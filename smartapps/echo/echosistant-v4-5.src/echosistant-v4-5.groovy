/* 
 * EchoSistant v4.5 - Total voice control of your SmartThings Home.
 
 
 ************************************ FOR INTERNAL USE ONLY ******************************************************
							
 								DON'T FORGET TO UPDATE RELEASE NUMBER!!!!!
 
 ************************************ FOR INTERNAL USE ONLY ******************************************************
 *		04/01/2018		Version:4.5 R.0.0.1a	Integrating with Rooms Manager app by @Bangali
 *		04/01/2018		Version:4.5 R.0.0.1		New Release - New Name
 *
 *  Copyright 2018 Jason Headley & Bobby Dobrescu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 * //UPDATE VERSION
/**********************************************************************************************************************************************/
import groovy.json.*
import java.text.SimpleDateFormat
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.InvalidKeyException
import java.security.MessageDigest

include 'asynchttp_v1'

definition(
	name			: "EchoSistant v4.5",
    namespace		: "Echo",
    author			: "JH/BD",
	description		: "Total voice control of your SmartThings Home.",
	category		: "My Apps",
    singleInstance	: true,
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************
	UPDATE LINE 38 TO MATCH RECENT RELEASE
**********************************************************************************************************************************************/
private def textVersion() {
	def text = "1.0"
}
private release() {
    def text = "R.0.0.1a"
}
/**********************************************************************************************************************************************/
preferences {   
    page name: "mainParentPage"
    page name: "mIntent"				
    page name: "mDevices"
    page name: "mDefaults" 
    page name: "mSHMSec"
    page name: "mNotifyProfile" 
    page name: "mProfiles" 
    page name: "mSupport"
    page name: "mSettings"
    page name: "mSkill"
    page name: "mControls"
    page name: "mDeviceDetails" 
    page name: "mTokens"
    page name: "mConfirmation"            
    page name: "mTokenReset"
    page name: "mBonus"
    page name: "mDashboard"
    page name: "mDashConfig"
    page name: "pageTwo"
    page name: "mWeatherConfig"

}

//dynamic page methods
page name: "mainParentPage"
def mainParentPage() {	
    dynamicPage(name: "mainParentPage", title:"", install: true, uninstall:false) {
        section ("") {
            href "mProfiles", title: "Create and Manage Rooms", description: mRoomsD(), state: mRoomsS(),
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
        }
        section ("") {
            href "mIntent", title: "Configure System Settings and Defaults", description: mIntentD(), state: mIntentS(), 
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"
        }
        section ("") {    
            paragraph "The Current Mode is: ${location.currentMode}"
            paragraph "Smart Home Monitor is set to: " + getSHMStatus() //location.currentState("alarmSystemStatus")?.value
        }
	}
}

page name: "mIntent"
def mIntent() {
    dynamicPage (name: "mIntent", title: "Configure System Security and Defaults", install: false, uninstall: false) {
        section ("") {
            href "mDefaults", title: "System and Device Defaults", description: mDefaultsD(), state: mDefaultsS(),
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
            }
        section ("") {
            href "mSecurity", title: "Voice Companion Security Options", description: mSecurityD(), state: mSecurityS(),
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Rest.png"
        }
        section ("") {
            href "mSupport", title: "Install and Support Information", description: mSupportD(), state: mSupportS(),
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_About.png"
        }
    }
}


page name: "mDefaults"
def mDefaults(){
    dynamicPage(name: "mDefaults", title: "", uninstall: false){
        section ("General Control") {            
            input "cLevel", "number", title: "Alexa Adjusts Light Levels by using a scale of 1-10 (default is +/-3)", defaultValue: 3, required: false
            input "cVolLevel", "number", title: "Alexa Adjusts the Volume Level by using a scale of 1-10 (default is +/-2)", defaultValue: 2, required: false
            input "cTemperature", "number", title: "Alexa Automatically Adjusts temperature by using a scale of 1-10 (default is +/-1)", defaultValue: 1, required: false						
        }
        section ("Fan Controls") {            
            input "cHigh", "number", title: "Alexa Adjusts High Level to 99% by default", defaultValue: 99, required: false
            input "cMedium", "number", title: "Alexa Adjusts Medium Level to 66% by default", defaultValue: 66, required: false
            input "cLow", "number", title: "Alexa Adjusts Low Level to 33% by default", defaultValue: 33, required: false
            input "cFanLevel", "number", title: "Alexa Automatically Adjusts Ceiling Fans by using a scale of 1-100 (default is +/-33%)", defaultValue: 33, required: false
        }
    }
}

page name: "mSecurity"    
def mSecurity(){
    dynamicPage(name: "mSecurity", title: "",install: false, uninstall: false) {
/*        section ("Set PIN Number to Unlock Security Features") {
            input "cPIN", "password", title: "Use this PIN for ALL Alexa Controlled Controls", defaultValue: false, required: false, submitOnChange: true
        }
        if (cPIN){
            section("") {
                href "pRestrict", title: "Only prompt for PIN number when...", description: pRestrictComplete(), state: pRestrictSettings()
            }
            section ("") {
                input "cMiscDev", "capability.switch", title: "Allow these Switches to be PIN Protected...", multiple: true, required: false, submitOnChange: true
                input "uPIN_Mode", "bool", title: "Enable PIN for Location Modes?", default: false, submitOnChange: true
                if(uPIN_Mode == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Location Modes"} 
                if (cMiscDev != null) 			{input "uPIN_S", "bool", title: "Enable PIN for Switch(es)?", defaultValue: false, submitOnChange: true}
                if(uPIN_S == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Switches"} 
                if (cTstat != null) 			{input "uPIN_T", "bool", title: "Enable PIN for Thermostats?", defaultValue: false, submitOnChange: true}
                if(uPIN_T == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Thermostats"}                             
                if (cDoor != null || cRelay != null) 	{input "uPIN_D", "bool", title: "Enable PIN for Doors?", defaultValue: false, submitOnChange: true}
                if(uPIN_D == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Doors"}                             
                if (cLock != null) 				{input "uPIN_L", "bool", title: "Enable PIN for Locks?", defaultValue: false, submitOnChange: true}
                if(uPIN_L == true)  {paragraph "You can also say: Alexa enable/disable the pin number for Locks"}                             
            }
        }*/
        section ("Smart Home Monitor Status Change Feedback",hideable: true){
            input "fSecFeed", "bool", title: "Activate SHM status change announcements.", default: false
            if (fSecFeed) {    
                input "shmSynthDevice", "capability.speechSynthesis", title: "Announce Changes On this Speech Synthesis Type Devices", multiple: true, required: false
                input "shmSonosDevice", "capability.musicPlayer", title: "Announce Changes On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true    
            }
            if (shmSonosDevice) {
                input "volume", "number", title: "Temporarily change volume", description: "0-100%", required: false
            }
        }
    }
}
		
      

page name: "mProfiles"    
def mProfiles() {
    dynamicPage (name: "mProfiles", title: "Create and Manage Rooms", install: true, uninstall: false) {
        if (childApps?.size()>0) {  
            section("",  uninstall: false){
                app(name: "EchoSistant Rooms v4.5", appName: "EchoSistant Rooms v4.5", namespace: "Echo", title: "Create a New Room", displayChildApps: false, multiple: true,  uninstall: false)
            }
        }
        else {
            section("",  uninstall: false){
                paragraph "NOTE: Looks like you haven't created any Rooms yet.\n \nPlease make sure you have installed the EchoSistant Rooms Add-on before creating a new Profile!"
                app(name: "EchoSistant Rooms v4.5", appName: "EchoSistant Rooms v4.5", namespace: "Echo", title: "Create a New Room", multiple: true,  uninstall: false)
            }
        }
    }
}  

page name: "mSettings"  
def mSettings(){
    dynamicPage(name: "mSettings", uninstall: true) {
        section("") {
            input "debug", "bool", title: "Enable Debug Logging", default: true, submitOnChange: true 
        }
        section (""){
            input "ShowLicense", "bool", title: "Show Apache License", default: false, submitOnChange: true
            def msg = textLicense()
            if (ShowLicense) paragraph "${msg}"
        }
        section ("Security Tokens", hideable: true, hidden: true) {
        	log.info "The information below is required to be copy and pasted into the AWS Lambda file. \n" +
    				"\n---------------------------------------------------------------------------------------\n" +
                    "\nvar STappID = '${app.id}' \n var STtoken = '${state.accessToken}';\n" +
                   	"var url= '${apiServerUrl("/api/smartapps/installations/")}' + STappID + '/' ;\n" +
                    "\n---------------------------------------------------------------------------------------"
            paragraph "The information below is required to be copy and pasted into the AWS Lambda file. \n" +
                "------------------------------------------------------------------------------------------------------------------------------------\n" +
                " var SmartThings Token = '${state.accessToken}' ;\n" +
                " var url = '${getApiServerUrl()}/${hubUID}/apps/${app.id}/' ;\n" +
                "------------------------------------------------------------------------------------------------------------------------------------" 
            href "mTokens", title: "Revoke/Reset Security Access Token"
        }
        section("Tap below to remove the ${textAppName()} application.  This will remove ALL Profiles and the App from your SmartThings Environment."){
        }	
    }             
}

page name: "mTokens"
def mTokens(){
    dynamicPage(name: "mTokens", title: "Security Tokens", uninstall: false){
        section(""){
            paragraph "Tap below to Reset/Renew the Security Token. You must log in to the IDE and open the Live Logs tab before tapping here. "+
                "Copy and paste the displayed tokens into your Amazon Lambda Code."
            if (!state.accessToken) {
                OAuthToken()
                paragraph "You must enable OAuth via the IDE to setup this app"
            }
        }
        def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. "+
            "OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
        section ("Reset Access Token / Application ID"){
            href "mConfirmation", title: "Reset Access Token and Application ID", description: none
        }
    }
} 

page name: "mConfirmation"
def mConfirmation(){
    dynamicPage(name: "mConfirmation", title: "Reset/Renew Access Token Confirmation", uninstall: false){
        section {
            href "mTokenReset", title: "Reset/Renew Access Token", description: "Tap here to confirm action - READ WARNING BELOW"
            paragraph "PLEASE CONFIRM! By resetting the access token you will disable the ability to interface this SmartApp with your Amazon Echo."+
                "You will need to copy the new access token to your Amazon Lambda code to re-enable access." +
                "Tap below to go back to the main menu with out resetting the token. You may also tap Done above."
        }
        section(" "){
            href "mainParentPage", title: "Cancel And Go Back To Main Menu", description: none 
        }
    }
}

page name: "mTokenReset"
def mTokenReset(){
    dynamicPage(name: "mTokenReset", title: "Access Token Reset", uninstall: false){
        section{
         //       revokeToken()
            state.accessToken = null
            OAuthToken()
            def msg = state.accessToken != null ? "New access token:\n${state.accessToken}\n\n" : "Could not reset Access Token."+
                "OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
            paragraph "${msg}"
            paragraph "The new access token and app ID are now displayed in the Live Logs tab of the IDE."
            log.info "New IDs: appID = '${app.id}' , Ttoken = '${state.accessToken}'"
        }
        section(" "){ 
            href "mainParentPage", title: "Tap Here To Go Back To Main Menu", description: none 
        }
    }
}

page name: "mSupport"  
def mSupport(){
    dynamicPage(name: "mSupport", uninstall: false) {
        section ("") {
            href "mSettings", title: "Security Token, Logging, App Uninstall", description: mSettingsD(), state: mSettingsS()
        }
//        section ("") {
//            href "mSkill", title: "Tap to view setup data for the AWS Main Intent Skill...", description: "", state: "complete"
//        }
        section ("") { 
            href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant", title: "Tap to go to the EchoSistant Wiki", description: "", state: "complete"
        }   
        section ("") {
            href url:"https://aws.amazon.com/lambda/", title: "Tap to go to the AWS Lambda Website", description: "", state: "complete"
        }
        section ("") {    
            href url:"https://developer.amazon.com/", title: "Tap to go to Amazon Developer website", description: "", state: "complete"
        }
        section (""){  
            paragraph ("You can reach out to the Echosistant Developers with the following information: \n" + 
                       "Jason Headley \n"+
                       "Forum user name @bamarayne \n" +
                       "Bobby Dobrescu \n"+
                       "Forum user name @SBDobrescu")
        }
    }	            	
}   


/*************************************************************************************************************
   CREATE INITIAL TOKEN
************************************************************************************************************/
def OAuthToken(){
	try {
		createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) {
		log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
	}
}
/*************************************************************************************************************
   LAMBDA DATA MAPPING
************************************************************************************************************/
mappings {
//	path("/cntrlList") {action: [GET: "controlList"]}	
//    path("/devList") {action: [GET: "deviceList"]}
    path("/b") { action: [GET: "processBegin"]}
//	path("/c") { action: [GET: "controlDevices"] }
//	path("/f") { action: [GET: "feedbackHandler"] }
//	path("/s") { action: [GET: "controlSecurity"] }
	path("/t") { action: [GET: "processTts"] }
}


/************************************************************************************************************
		Base Process
************************************************************************************************************/
def installed() {
	if (debug) log.debug "Installed with settings: ${settings}"
    state.ParentRelease = release()
    //Reminders
    state.esEvent = [:]
}
def updated() { 
	if (debug) log.debug "Updated with settings: ${settings}"
    unsubscribe()
    state.esEvent = [:]
    initialize()
}
def initialize() {
		//WEBCORE
        webCoRE_init()
		//REMINDERS
		state.vcProfiles = state.vcProfiles ? state.vcProfiles : []      
		sendLocationEvent(name: "EchoSistant v4.5", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "Reminders list refresh")
		//SHM status change and keypad initialize
    		subscribe(location, locationHandler)
            subscribe(location, "alarmSystemStatus",alarmStatusHandler) //used for ES speaker feedback
            state.esProfiles = state.esProfiles ? state.esProfiles : []

			state.lambdaReleaseTxt = "Not Set"
            state.lambdaReleaseDt = "Not Set" 
            state.lambdatextVersion = "Not Set"
        //Alexa Responses
			state.pTryAgain = false
        	state.pContCmds = settings.pDisableContCmds == false ? true : settings.pDisableContCmds == true ? false : true
            state.pMuteAlexa = settings.pEnableMuteAlexa
			state.pShort = settings.pUseShort
            state.pContCmdsR = "init"       
        //PIN Settings
            state.usePIN_T = settings.uPIN_T
            state.usePIN_L = settings.uPIN_L
            state.usePIN_D = settings.uPIN_D
            state.usePIN_S = settings.uPIN_S             
			state.usePIN_SHM = settings.uPIN_SHM
            state.usePIN_Mode = settings.uPIN_Mode
            state.savedPINdata = null
            state.pinTry = null
        //Other Settings
            state.lastAction = null
			state.lastActivity = null
			state.pendingConfirmation = false
            unschedule("startLoop")
            unschedule("continueLoop")
}
def getProfileList(){
		return getChildApps()*.label
}

/************************************************************************************************************
		Begining Process - Lambda via page b
************************************************************************************************************/
def processBegin(){ 
    def versionTxt  = params.versionTxt 		
    def versionDate = params.versionDate
    def releaseTxt = params.releaseTxt
    def event = params.intentResp
        state.lambdaReleaseTxt = releaseTxt
        state.lambdaReleaseDt = versionDate
        state.lambdatextVersion = versionTxt
    def versionSTtxt = textVersion()
    def releaseSTtxt = release()
    def pPendingAns = false 
    def pContinue = state.pMuteAlexa
    def pShort = state.pShort
    def String outputTxt = (String) null 
    	state.pTryAgain = false
    if (debug) log.debug "^^^^____LAUNCH REQUEST___^^^^" 
    if (debug) log.debug "Launch Data: (event) = '${event}', (Lambda version) = '${versionTxt}', (Lambda release) = '${releaseTxt}', (ST Main App release) = '${releaseSTtxt}'"

    if (event == "noAction") {
    	state.pinTry = null
        state.savedPINdata = null
        state.pContCmdsR = null 
        state.pTryAgain = false
    }
    
  
    
    
// >>> NO Intent <<<<    
    if (event == "AMAZON.NoIntent"){
    	if(state.pContCmdsR == "level" || state.pContCmdsR == "repeat"){
            if (state.lastAction != null) {
            	if (state.pContCmdsR == "level") {state.pContCmdsR = "repeat"}
                def savedData = state.lastAction
                outputTxt = controlHandler(savedData) 
                pPendingAns = "level"
            }
            else {
                state.pContCmdsR = null
                pPendingAns = null
            }
        }
        if( state.pContCmdsR == "door"){
            if (state.lastAction != null) {
                state.lastAction = null
                state.pContCmdsR = null 
                pPendingAns = null 
            }
        }
        if( state.pContCmdsR == "feedback" ||  state.pContCmdsR == "bat" || state.pContCmdsR == "act" ){
            if (state.lastAction != null) {
                state.lastAction = null
                state.pContCmdsR = null 
                pPendingAns = null 
            }
        }
        if( state.pContCmdsR == "init" || state.pContCmdsR == "undefined"){
        	state.pTryAgain = false
        }
        if( state.pContCmdsR == null){
        	state.pTryAgain = false
        }
    }
// >>> YES Intent <<<<     
    if (event == "AMAZON.YesIntent") {
        if (state.pContCmdsR == "level" || state.pContCmdsR == "repeat") {
            state.pContCmdsR = null
            state.lastAction = null
            pPendingAns = "level"
        }
        else {
        	state.pTryAgain = false
        }
        if(state.pContCmdsR == "door"){
            if (state.lastAction != null) {
                def savedData = state.lastAction
 				//NEW PIN VALIDATION!!!!! ///// ADD THE THE usePIN variable below to run the PIN VALIDATION
 				if(state.usePIN_D == true) {
     				//RUN PIN VALIDATION PROCESS
                	def pin = "undefined"
               		def command = "validation"
                	def num = 0
                	def unit = "doors"
                	outputTxt = pinHandler(pin, command, num, unit)
                    pPendingAns = "pin"
                    if (state.pinTry == 3) {pPendingAns = "undefined"}
                    log.warn "try# ='${state.pinTry}'"
					return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            	}
                else {
                outputTxt = controlHandler(savedData) 
                pPendingAns = "door"
            	}
              return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
        	}
        }
        if(state.pContCmdsR == "feedback"){
            if (state.lastAction != null) {
                def savedData = state.lastAction
                outputTxt = getMoreFeedback(savedData) 
                pPendingAns = "feedback"
				return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
         }
         if(state.pContCmdsR == "bat" || state.pContCmdsR == "act"){
            if (state.lastAction != null) {
                def savedData = state.lastAction
                outputTxt = savedData
                pPendingAns = "feedback"
                state.pContCmdsR = null
				return ["outputTxt":outputTxt, "pContinue":pContinue,  "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
       }
       if(state.pContCmdsR == "caps"){
            if (state.lastAction!= null) {
                outputTxt = state.lastAction
                pPendingAns = "caps"
				state.pContCmdsR = null 
				state.lastAction = null
                return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
            }
        }        
     }
// >>> Handling a Profile Intent <<<<      
     if (!event.startsWith("AMAZON") && event != "main" && event != "security" && event != "feedback" && event != "profile" && event != "noAction"){
		childApps?.each {child ->
			if (child?.label.toLowerCase() == event?.toLowerCase()) { 
                pContinue = child?.checkState()  
            }
       	}
        //if Alexa is muted from the child, then mute the parent too / MOVED HERE ON 2/9/17
        pContinue = pContinue == true ? true : state.pMuteAlexa == true ? true : pContinue
		return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]	     
	}
	if (debug){
    	log.debug "Begining Process data: (event) = '${event}', (ver) = '${versionTxt}', (date) = '${versionDate}', (release) = '${releaseTxt}'"+ 
      	"; data sent: pContinue = '${pContinue}', pShort = '${pShort}',  pPendingAns = '${pPendingAns}', versionSTtxt = '${versionSTtxt}', releaseSTtxt = '${releaseSTtxt}' outputTxt = '${outputTxt}' ; "+
        "other data: pContCmdsR = '${state.pContCmdsR}', pinTry'=${state.pinTry}' "
	}
    return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]	 

} 

/************************************************************************************************************
   Get current SHM status for Dashboard
************************************************************************************************************/
def getSHMStatus() {
    def currentSHM = location.currentState("alarmSystemStatus")?.value
    if (currentSHM == "off") {
        currentSHM = "Security Disabled" }
    if (currentSHM == "away") {
        currentSHM = "Armed-Away" }
    if (currentSHM == "stay") {
        currentSHM = "Armed-Stay" }
    return currentSHM
}
    
/************************************************************************************************************
   SECURITY CONTROL - from Lambda via page s
************************************************************************************************************/
def controlSecurity(param) {
		//FROM LAMBDA
        def command = params.sCommand
        def num = params.sNum
        def sPIN = params.sPIN
        def type = params.sType
        def control = params.sControl       
		def pintentName = params.intentName
        //FROM CONTROL MODULE 
        def cCommand = param?.command
        def cNum = param?.num      
		def cPintentName = param?.pintentName        
        log.warn "cCommand = ${cCommand},cNum = ${cNum}, cPintentName = ${cPintentName}"
        
		command = command == "null" ? "undefined" : command      
        num = num == "null" ? "undefined" : num      
        sPIN = sPIN == "null" ? "undefined" : sPIN      
        type = type == "null" ? "undefined" : type      
        control = control == "null" ? "undefined" : control   

        if(cCommand){
        	command = cCommand
            num = cNum
            pintentName = cPintentName
        }
		log.warn "command = ${command},num = ${num}, pintentName = ${pintentName}"
        //OTHER VARIABLES
        def String outputTxt = (String) null 
		def pPIN = false
        def String secCommand = (String) null
        def delay = false
        def data = [:]
        	control = control?.replaceAll("[^a-zA-Z0-9]", "")
        	sPIN = sPIN == "?" ? "undefined" : sPIN
        if (num == "undefined" || num =="?") {num = 0 } 
        	num = num as int
        
        if (debug) log.debug "System Control Data: (sCommand)= ${command},(sNum) = '${num}', (sPIN) = '${sPIN}'," +
        					 " (type) = '${type}', (sControl) = '${control}',(pintentName) = '${pintentName}'"
	def sProcess = true
    state.pTryAgain = false
//try {   
    if (pintentName == "security") { 
    log.warn "security intent"
		if (ptts == "this is a test"){
			outputTxt = "Congratulations! Your EchoSistant is now setup properly" 
			return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]       
    	}
        def modes = location.modes.name
    	def currMode = location.currentMode
    	def routines = location.helloHome?.getPhrases()*.label
    	def currentSHM = location.currentState("alarmSystemStatus")?.value
    	// HANDLING SHM
        if (type != "mode") { 
            if (control == "status") {      
                    currentSHM = currentSHM == "off" ? "disabled" : currentSHM == "away" ? "Armed Away" : currentSHM == "stay" ? "Armed Home" : "unknown"
                    outputTxt = "Your Smart Home Monitor Status is " +  currentSHM
                    state.pTryAgain = false
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }
            if (command == "cut off" || command == "cancel" || command == "stop" || command == "disable" || command == "deactivate" || command == "off" || command == "disarm") {
            log.warn "command disarm"
                secCommand = currentSHM == "off" ? null : "off"
                    if (secCommand == "off"){
                    delay = false
                    data = [command: secCommand, delay: delay]
                    if(cPIN && state.usePIN_SHM == true){
                        state.lastAction = data
                        state.pContCmdsR = "security"
                        //RUN PIN VALIDATION PROCESS
                        def pin = "undefined"
                        command = "validation"
                        def unit = "security"
                        outputTxt = pinHandler(pin, command, num, unit)
                        pPIN = true
                        if (state.pinTry == 3) {pPIN = false}
                        log.warn "try# ='${state.pinTry}'"
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                    else {               
                        outputTxt = securityHandler(data)
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                }
                else {
                outputTxt = "The Smart Home Monitor is already set to " + command
                state.pContCmdsR = "undefined"
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }
            }
            if (command == "start" || command == "enable" || command == "activate" || command == "schedule" || command == "arm" || command == "on") {
            log.warn "command arm"
                if(control == "stay" || control == "away") {  
                    secCommand = control == "stay" ? "stay" : control == "away" ? "away" : control
                    def process = true
                }
                else {
                    outputTxt = "Are you staying home or leaving?"
                    state.pContCmdsR = "stayORleave"
                    def process = false
                    state.lastAction = num
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }     
            }	
            if(process == true || control.contains("staying") || control == "leaving" || control == "stay" || control == "away") {
                secCommand = control.contains("staying") ? "stay" : control == "leaving" ? "away" : control == "stay" ? "stay" : control == "away" ? "away" : secCommand
                if (state.pContCmdsR == "stayORleave") {num = state.lastAction}           
                if (num > 0) {               
                    def numText = getUnitText ("minute", num)
                    delay = true
                    data = [command: secCommand, delay: delay]
                    runIn(num*60, securityHandler, [data: data])
                    outputTxt = "Ok, changing the Smart Home Monitor to armed $secCommand in " + numText.text
                    state.pContCmdsR = "undefined"
                    state.pTryAgain = false
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }            
                else {
                    delay = false
                    data = [command: secCommand, delay: delay]			
                    outputTxt = securityHandler(data)
                    state.pContCmdsR = "undefined"
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
               }
            }
        }
        else {
        	if(currMode != control){
                modes?.find { m -> 
					def mMatch = m.replaceAll("[^a-zA-Z0-9]", "")
                    if(mMatch.toLowerCase() == control.toLowerCase()) {
                        if(currMode !=  m) {
                            if(cPIN && state.usePIN_Mode == true) {
                                delay = false
                                data = [command: m, delay: delay]
                                state.lastAction = data
                                state.pContCmdsR = "mode"
                                //RUN PIN VALIDATION PROCESS
                                def pin = "undefined"
                                command = "validation"
                                def unit = "mode"
                                outputTxt = pinHandler(pin, command, num, unit)
                                pPIN = true
                                if (state.pinTry == 3) {pPIN = false}
                                log.warn "try# ='${state.pinTry}'"                        
                            }
                            else { 
                                location.setMode(m)
                                outputTxt = "I changed your location mode to " + control
                            }
                        }
                        else {
                            outputTxt = "Your location mode is already " + control
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
               		}
               }
               if (outputTxt != null) {
               return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            	} 
            }
            else {
                outputTxt = "Your location mode is already " + control
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        	}
        }
		if (control != "undefined") {	
            routines?.find {r -> 
                def rMatch = r.replaceAll("[^a-zA-Z0-9]", "")
            	if(rMatch.toLowerCase() == control.toLowerCase()){
                   		if(cPIN && cRoutines) {
                         	def pinRoutine =  cRoutines.find {r1 -> r1 == r}  
                            if (pinRoutine) {
                                delay = false
                                data = [command: r, delay: delay]
                                state.lastAction = data
                                state.pContCmdsR = "routine"
                                //RUN PIN VALIDATION PROCESS
                                def pin = "undefined"
                                command = "validation"
                                def unit = "routine"
                                outputTxt = pinHandler(pin, command, num, unit)
                                pPIN = true
                                if (state.pinTry == 3) {pPIN = false}
                                log.warn "try# ='${state.pinTry}'"                        
                            }
                            /* faluty logic ... removed 2/20/17 Bobby
                            else {
                            	log.warn "running routine = ${r}"
                                location.helloHome?.execute(r)
                                outputTxt = "Ok, I am running the " + control + " routine"
                                log.warn " outputTxt = ${outputTxt}"
                            }
                            */
                    	}
                        else {
                                location.helloHome?.execute(r)
                                outputTxt = "Ok, I am running the " + control + " routine"
                    	}
            	}
			}
         	if (outputTxt != null) {
            	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
           }   
    	}
        def hText = type != "undefined" ? "control " + type : control != "undefined" ? "manage " + control +  " as a system control" : "manage your system controls" 
        def sText = type != "undefined" ? type : control != "undefined" ? control : "something"  
			if (state.pShort != true){ 
				outputTxt = "Sorry, I heard that you were looking to " + hText + " but Echosistant wasn't able to take any actions"
			}
			else {outputTxt = "I've heard " + sText +  " but I wasn't able to manage your system controls "} 
            state.pTryAgain = true
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    }

    } 
    /*catch (Throwable t) {
        log.error t
        outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
	}
}	*/ 

/************************************************************************************************************
	SECURITY CONTROL HANDLER
************************************************************************************************************/ 
def securityHandler(data) {
	def sCommand = data.command
    def sDelay = data.delay
    def String result = (String) "Command " + sCommand + " is not supported by Smart Home Monitor"
	def currentSHM = location.currentState("alarmSystemStatus")?.value
    if (sCommand == "stay" || sCommand == "away" || sCommand == "off"){
        if (sCommand != currentSHM) {
            sendLocationEvent(name: "alarmSystemStatus", value: sCommand)
            if (sDelay == false) {
                if(sCommand == "away" || sCommand == "stay") 	{result = "I changed the Smart Home Monitor to " + sCommand }
                if(sCommand == "off") 	{result = "I disarmed the Smart Home Monitor" }
                return result
            }
        }
        else {
            result = "The Smart Home Monitor is already set to " + sCommand
            state.pContCmdsR = "undefined"
            return result
        }
    }
    return result
}
/************************************************************************************************************
   TEXT TO SPEECH PROCESS - Lambda via page t
************************************************************************************************************/
def processTts() {
		//LAMBDA VARIABLES
    	def ptts = params.ttstext 
        def pintentName = params.intentName
        //OTHER VARIABLES
        def String outputTxt = (String) null 
 		def String pContCmdsR = (String) null
        def pContCmds = false
        def pTryAgain = false
        def pPIN = false
        def dataSet = [:]
        if (debug) log.debug "Messaging Profile Data: (ptts) = '${ptts}', (pintentName) = '${pintentName}'"   
                
        pContCmdsR = "profile"
		def tProcess = true
//try {

	if (ptts == "this is a test"){
		outputTxt = "Congratulations! EchoSistant is now setup properly" 
		return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]       
    }
        
        if(ptts.contains("no ") || ptts == "no" || ptts == "stop" || ptts == "cancel" || ptts == "kill it" || ptts == "zip it" || ptts == "yes" && state.pContCmdsR != "wrongIntent"){
        	if(ptts == "no" || ptts == "stop" || ptts == "cancel" || ptts == "kill it" || ptts == "zip it" || ptts.contains("thank")){
                outputTxt = "ok, I am here if you need me"
                pContCmds = false
                return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        	}
			else {
                outputTxt = "ok, please continue, "
                pContCmds = false
                return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        	}        
        }
  		else{
             childApps.each {child ->
             	if (child.label.toLowerCase() == pintentName.toLowerCase()) { 
                    if (debug) log.debug "Found a profile: '${pintentName}'"
                    pintentName = child.label
                    // recording last message
                    state.lastMessage = ptts
                    state.lastIntent = pintentName
                    state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
                    dataSet = [ptts:ptts, pintentName:pintentName, fDevice:fDevice] 
					def childRelease = child.checkRelease()
					log.warn "childRelease = $childRelease"

					if (ptts.startsWith("tell") || ptts.startsWith("get") || ptts.endsWith("tonight") || ptts.contains("weather") || ptts.contains("temperature") || ptts.contains("forecast") || ptts.contains("humidity") || ptts.contains("rain") || ptts.contains("wind")) {
                    	def pResponse = child.profileFeedbackEvaluate(dataSet)
                        outputTxt = pResponse.outputTxt
                    	pContCmds = pResponse.pContCmds
                    	pContCmdsR = pResponse.pContCmdsR
                    	pTryAgain = pResponse.pTryAgain
                    	}
					if (ptts.startsWith("for") || ptts.startsWith("is") || ptts.startsWith("has") || ptts.startsWith("give") || ptts.startsWith("how") || ptts.startsWith("what") || ptts.startsWith("when") || ptts.startsWith("which") || ptts.startsWith("are") || ptts.startsWith("check") || ptts.startsWith("who")) {
                        def pResponse = child.profileFeedbackEvaluate(dataSet)
                        outputTxt = pResponse.outputTxt
                    	pContCmds = pResponse.pContCmds
                    	pContCmdsR = pResponse.pContCmdsR
                    	pTryAgain = pResponse.pTryAgain
                    	}
                    else {  
                        def pResponse = child.profileEvaluate(dataSet)
                		outputTxt = pResponse.outputTxt
                    	pContCmds = pResponse.pContCmds
                    	pContCmdsR = pResponse.pContCmdsR
                    	pTryAgain = pResponse.pTryAgain
                    	log.info "I have received this from the Lambda: ${outputTxt}"
                    	}
                	}
            	}

            if (outputTxt?.size()>0){
                return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
            }
            else {
                if (state.pShort != true){
                	outputTxt = "Hey, I wish I could help, but EchoSistant couldn't find a Profile named " + pintentName + " or the command may not be supported"
                }
                else {outputTxt = "I've heard " + pintentName + " , but I wasn't able to take any actions "} 
                pTryAgain = true
                return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain": pTryAgain, "pPIN":pPIN]
            }
        	
            def hText = "run a messaging and control profile"
			if (state.pShort != true){ 
				outputTxt = "Sorry, I heard that you were looking to " + hText + " but EchoSistant wasn't able to take any actions "
			}
			else {outputTxt = "I've heard " + pintentName + " , but I wasn't able to take any actions "}         
			pTryAgain = true
			return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]              
    	}

} 

/***********************************************************************************************************
		SMART HOME MONITOR STATUS AND KEYPAD HANDLER
***********************************************************************************************************/
// ALARM STATUS CHANGE FEEDBACK TO SPEAKERS
def alarmStatusHandler(evt) {
	if (fSecFeed) {
	def curEvtValue = evt.value
	log.info "Smart Home Monitor status changed to: ${curEvtValue}"
		if (shmSynthDevice || shmSonosDevice) {
			if (evt.value == "armAway") {
            	sendAwayCommand
            	if(shmSynthDevice) shmSynthDevice?.speak("Attention, The alarm system has changed status to armed away")
            	if (shmSonosDevice) 
             	shmSonosDevice?.playTextAndRestore("Attention, The alarm system has changed status to armed away")
            	}
                else if (evt.value == "stay") {
                	if(shmSynthDevice) shmSynthDevice?.speak("Attention, The alarm system has changed status to armed home'")
            		if (shmSonosDevice) 
             		shmSonosDevice?.playTextAndRestore("Attention, The alarm system has changed status to armed home")
            		}
                    else if(evt.value == "off") {
                    	if(shmSynthDevice) shmSynthDevice?.speak("Attention, The alarm system has changed status to disarmed")
            			if (shmSonosDevice) 
             			shmSonosDevice?.playTextAndRestore("Attention, The alarm system has changed status to disarmed")
            			}
					}
       			}
			}

/*^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
X 																											X
X                       					PRIVATE FUNCTIONS												X
X                        																					X
/*^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

/************************************************************************************************************
	CONTROL SUPPORT - PIN HANDLER
************************************************************************************************************/ 
private pinHandler(pin, command, num, unit) {
	def result
        def String pinNum = (String) null
		pinNum = num	
    if (command == "validation") {
		state.savedPINdata = state.lastAction
        state.lastAction = null
		result = "Pin number please"
		state.pinTry = 0
		if (debug) log.warn "PIN response pending - '${state.pinTry}'"  
        return result
	}        
    if (pin == cPIN || command == cPIN || pinNum == cPIN || unit == cPIN ) {
		def data = state.savedPINdata != null ? state.savedPINdata : lastAction
        state.pTryAgain = false
            if (data == "disablelocks" || data == "disablethermostats" || data == "disabledoors" || data == "disableswitches" || data == "disablesecurity" || data == "disablemodes"){ 
                if 		(data == "disablelocks")		{state.usePIN_L = false}
                else if (data == "disablethermostats") 	{state.usePIN_T = false}
                else if (data == "disabledoors") 		{state.usePIN_D = false}  
                else if (data == "disableswitches") 	{state.usePIN_S = false} 
                else if (data == "disablesecurity") 	{state.usePIN_SHM = false}
                else if (data == "disablemodes") 		{state.usePIN_Mode = false} 
                state.pinTry = null /// 2/8/2017
                result = "Ok, pin number for " + data.replace("disable", "") + " has been disabled.  To activate it again, just say enable the PIN number for " + data.replace("disable", "")   
            	return result
            }
            else {
				def pNum = data.num
        		def pUnit = data.unit           
                if(state.pContCmdsR == "security"){
                    result = securityHandler(data)
                    state.pinTry = null /// 2/8/2017
                    return result
                }
                if(state.pContCmdsR == "mode"){
                	def cmd = state.savedPINdata.command
                	location.setMode(cmd)
                	result = "I changed your location mode to " + cmd
                	state.pinTry = null /// 2/8/2017
                    return result
                }
                if(state.pContCmdsR == "routine"){ 
                	def cmd = state.savedPINdata.command
                	location.helloHome.execute(cmd)
                	result = "I executed your routine, " + cmd
                	state.pinTry = null /// 2/8/2017
                    return result
                }
                else {
                	if (state.pContCmdsR == "cMiscDev" && pNum > 0 && pUnit == "minutes") {
                		runIn(pNum*60, controlHandler, [data: data])
                        def getTxt = getUnitText(pUnit, pNum)     
        				def numText = getTxt.text
                        if (data.command == "on" || data.command == "off" ) {result = "Ok, turning " + data.device + " " + data.command + ", in " + numText}
						else if (data.command == "decrease") {result = "Ok, decreasing the " + data.device + " level in " + numText}
						else if (data.command == "increase") {result = "Ok, increasing the " + data.device + " level in " + numText}                        
                    	state.pContCmdsR = "undefined"
                        state.savedPINdata = null
                        state.pinTry = null /// 2/8/2017
                        return result
                	}
                	else {
                	result = controlHandler(data)
                    state.pinTry = null /// 2/8/2017
                    return result
                	}
              	}
            }
            state.savedPINdata = null
            state.pContCmdsR = "undefined"
            return result
	}
	else {
		state.pinTry = state.pinTry + 1
			if (state.pinTry < 4){
				result = "I'm sorry, that is incorrect "
				if (debug) log.debug "PIN NOT Matched! PIN = '${cPIN}', ctPIN= '${ctPIN}', ctNum= '${num}', ctCommand ='${command}', try# ='${state.pinTry}'"
				state.pTryAgain = true
                return result
			}
			else { 
				state.pinTry = null
                state.savedPINdata = null
                state.pTryAgain = false
				result = "I'm sorry, that is incorrect. Please check your pin number and try again later"
                return result
			}
	} 
}

/***********************************************************************************************************************
    MISC. - SCHEDULE HANDLER
***********************************************************************************************************************/
private scheduleHandler(unit) {
    def rowDate = new Date(now())
    def cDay = rowDate.date
    def cHour= rowDate.hours
	def cMin = rowDate.minutes   
    def result
    if (unit == "filters") {
    	if (debug) log.debug "Received filter replacement request"
        state.scheduledHandler = "filters"
        def xDays = settings.cFilterReplacement
        def tDays = new Date(now() + location.timeZone.rawOffset) + xDays 
        def schTime = tDays.format("h:mm aa")                       
		def schDate = tDays.format("EEEE, MMMM d")
       		runOnce(new Date() + xDays , "filtersHandler")
        	result = "Ok, scheduled reminder to replace the filters on " + schDate + " at " + schTime
        	state.filterNotif = "The filters need to be changed on  ${schDate}"
    		return result
    }
}
/***********************************************************************************************************************
    MISC. - FILTERS REMINDER
***********************************************************************************************************************/
private filtersHandler() {
    def tts = "It's time to replace your HVAC filters"
	if (synthDevice) {
    	synthDevice?.speak(tts) 
    }
    if (sonosDevice){
    	state.sound = textToSpeech(tts instanceof List ? tts[0] : tts)
        def currVolLevel = sonosDevice.latestValue("level")
        def newVolLevel = volume //-(volume*10/100)
        sonosDevice.setLevel(newVolLevel)
        sonosDevice.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
    }
	if(recipients?.size()>0 || sms?.size()>0){        
    	sendtxt(tts)
    }
}
/***********************************************************************************************************************
    MISC. - RUN REPORT FROM PROFILE
***********************************************************************************************************************/
def runReport(profile) {
def result
//    def file = profile//.toLowerCase()
	log.info "profile = $profile.label"
           		childApps.each {child ->
                        def ch = child.label//.toLowerCase()
                  //      log.info "ch = $ch"
                		if (ch == "${profile}") { 
                    		log.debug "Found a profile, $ch"
                            result = child.ttsActions(tts) //.initialize()//profileEvaluate()//child.profileEvaluate(ch) //runProfile(ch)
						}
                        else {
                        log.debug "Could not find a profile to run, $profile"
                        }
            	}
                return result
}

/*^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
X 																											X
X                       					UI FUNCTIONS													X
X                        																					X
/*^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
/************************************************************************************************************
   		UI - Version/Copyright/Information/Help
************************************************************************************************************/
private def textAppName() {
	def text = app.label // Parent Name
}
private def textLicense() {
	def text =
	"Licensed under the Apache License, Version 2.0 (the 'License'); "+
	"you may not use this file except in compliance with the License. "+
	"You may obtain a copy of the License at"+
	" \n"+
	" http://www.apache.org/licenses/LICENSE-2.0"+
	" \n"+
	"Unless required by applicable law or agreed to in writing, software "+
	"distributed under the License is distributed on an 'AS IS' BASIS, "+
	"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "+
	"See the License for the specific language governing permissions and "+
	"limitations under the License."
}

/************************************************************************************************************
		3RD Party Integrations
************************************************************************************************************/
private webCoRE_handle(){return'webCoRE'}
private webCoRE_init(pistonExecutedCbk){state.webCoRE=(state.webCoRE instanceof Map?state.webCoRE:[:])+(pistonExecutedCbk?[cbk:pistonExecutedCbk]:[:]);subscribe(location,"${webCoRE_handle()}.pistonList",webCoRE_handler);if(pistonExecutedCbk)subscribe(location,"${webCoRE_handle()}.pistonExecuted",webCoRE_handler);webCoRE_poll();}
private webCoRE_poll(){sendLocationEvent([name: webCoRE_handle(),value:'poll',isStateChange:true,displayed:false])}
public  webCoRE_execute(pistonIdOrName,Map data=[:]){def i=(state.webCoRE?.pistons?:[]).find{(it.name==pistonIdOrName)||(it.id==pistonIdOrName)}?.id;if(i){sendLocationEvent([name:i,value:app.label,isStateChange:true,displayed:false,data:data])}}
public  webCoRE_list(mode){def p=state.webCoRE?.pistons;if(p)p.collect{mode=='id'?it.id:(mode=='name'?it.name:[id:it.id,name:it.name])}}
public  webCoRE_handler(evt){switch(evt.value){case 'pistonList':List p=state.webCoRE?.pistons?:[];Map d=evt.jsonData?:[:];if(d.id&&d.pistons&&(d.pistons instanceof List)){p.removeAll{it.iid==d.id};p+=d.pistons.collect{[iid:d.id]+it}.sort{it.name};state.webCoRE = [updated:now(),pistons:p];};break;case 'pistonExecuted':def cbk=state.webCoRE?.cbk;if(cbk&&evt.jsonData)"$cbk"(evt.jsonData);break;}}



/************************************************************************************************************
   Page status and descriptions 
************************************************************************************************************/       
//	Naming Conventions: 
// 	description = pageName + D (E.G: description: mIntentD())
// 	state = pageName + S (E.G: state: mIntentS(),
/************************************************************************************************************/       
def pRestrictSettings(){ def result = "" 
	if (days || pinOnOpen || modes || runDay || startingX || endingX) {
    	result = "complete"}
        result}
def pRestrictComplete() {def text = "Tap here to configure" 
    if (days || pinOnOpen || modes || runDay || startingX || endingX) {
    	text = "Configured"}
    	else text = "Tap here to Configure"
        text}
/** Dashboard **/
mDashboardD
def mDashboardS() {
    def result = ""
    if (mLocalWeather || mWeatherConfig || ThermoStat1 || ThermoStat2 || tempSens1 || tempSens2 || tempSens3 || tempSens4 || tempSens5) {
    	result = "complete"
    }
    result
}
def mDashboardD() {
    def text = "View Current Weather and Active Weather Alerts"
    if (mLocalWeather || mWeatherConfig || ThermoStat1 || ThermoStat2 || tempSens1 || tempSens2 || tempSens3 || tempSens4 || tempSens5) { 
            text = "View Current Weather and Active Weather Alerts"
    }
    text
}
/** Main Profiles Page **/
def mIntentS(){
	def result = ""
    def IntentS = ""
    if (cSwitch || cFan || cDoor || cRelay || cTstat || cIndoor || cOutDoor || cVent || cMotion || cContact || cWater || cPresence || cSpeaker || cSynth || cMedia) {
    	IntentS = "comp"
        result = "complete"
    }    	
    	result
}
def mIntentD() {
    def text = "Tap here to Configure"
	def mIntentS = mIntentS()
    if (mIntentS) 
    {
        text = "Configured"
    }
    else text = "Tap here to Configure"
	    text
}  
/** Configure Profiles Pages **/
def mRoomsS(){
    def result = ""
    if (childApps?.size()) {
    	result = "complete"	
    }
    result
}
def mRoomsD() {
    def text = "No Profiles have been configured. Tap here to begin"
    def ch = childApps?.size()     
    if (ch == 1) {
        text = "One profile has been configured. Tap here to view and change"
    }
    else {
    	if (ch > 1) {
        text = "${ch} Profiles have been configured. Tap here to view and change"
     	}
    }
    text
}
/** General Settings Page **/
def mSettingsS() {
    def result = ""
    if (ShowLicense || debug) {
    	result = "complete"	
    }
    result
}
def mSettingsD() {
    def text = "Tap here to Configure"
    if (ShowLicense || debug) { 
            text = "Configured"
    }
    text
}
/** Install and Support Page **/
def mSupportS() {
    def result = ""
    if (notifyOn || securityOn) {
    	result = "complete"	
    }
    result
}
def mSupportD() {
    def text = ""
    if (notifyOn || securityOn) { 
            text = ""
    }
    text
}

/** Main Intent Page **/
def mDevicesS() {def result = ""
    if (cSwitch || cFan || cDoor || cRelay || cTstat || cIndoor || cOutDoor || cVent || cMotion || cContact || cWater || cPresence || cSpeaker || cSynth || cMedia) {
    	result = "complete"}
   		result}
def mDevicesD() {def text = "Tap here to configure settings" 
    if (cSwitch || cFan || cDoor || cRelay || cTstat || cIndoor || cOutDoor || cVent || cMotion || cContact || cWater || cPresence || cSpeaker || cSynth || cMedia) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}  
def mDefaultsS() {def result = ""
    if (cLevel || cVolLevel || cTemperature || cHigh || cMedium || cLow || cFanLevel || cInactiveDev || cFilterReplacement || cFilterSynthDevice || cFilterSonosDevice) {
    	result = "complete"}
   		result}
def mDefaultsD() {def text = "Tap here to configure settings" 
    if (cLevel || cVolLevel || cTemperature || cHigh || cMedium || cLow || cFanLevel || cInactiveDev || cFilterReplacement || cFilterSynthDevice || cFilterSonosDevice) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}         
def mSecurityS() {def result = ""
                  if (ctStat || cPIN || uPIN_Mode) { // || uPIN-S || uPIN-T) { // || uPIN-D) { // || uPIN-L) {
    	result = "complete"}
   		result}
def mSecurityD() {def text = "Tap here to configure settings" 
                  if (ctStat || cPIN || uPIN_Mode) { // || uPIN-S || uPIN-T) { // || uPIN-D) { // || uPIN-L) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}
        
