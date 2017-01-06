/* 
 * EchoSistant - The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enabled Device.
 *
 *		12/31/2016		Release 4.1.1	New features: status updates, custom commands, weather alerts, message reminders 
 *										Improvements: streamlined UI and processing
 *
 *  Copyright 2016 Jason Headley & Bobby Dobrescu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
/**********************************************************************************************************************************************/
definition(
	name			: "EchoSistantLabs",
    namespace		: "EchoLabs",
    author			: "JH/BD",
	description		: "The Ultimate Voice Controlled Assistant Using Alexa Enabled Devices.",
	category		: "My Apps",
    singleInstance	: true,
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/
preferences {   
    page name: "mainParentPage"
    		page name: "mIntent"
				page name: "mDevices"
                page name: "mFeedback"
                page name: "mDefaults"            
    		page name: "mProfiles"
            page name: "mSettings"
           		page name: "mSkill"
            		page name: "mProfileDetails"
            		page name: "mDeviceDetails" 
                page name: "mTokens"
                    page name: "mConfirmation"            
                    	page name: "mTokenReset"            
}            
//dynamic page methods
page name: "mainParentPage"
    def mainParentPage() {	
       dynamicPage(name: "mainParentPage", title:"", install: true, uninstall:false) {
       		section ("") {
                href "mIntent", title: "Main Home",
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png"    
				href "mProfiles", title: "Room Details",
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_msg.png"
				href "mSettings", title: "General Settings",
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"
            }
		}
	}
page name: "mIntent"
    def mIntent() {
    	dynamicPage (name: "mIntent", title: "", install: false, uninstall: false) {
			section("") {
	            href "mDevices", title: "Select Devices",
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"    
			}
			section("") {
	            href "mFeedback", title: "Feedback Options",
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
			}                
            section ("") {
                href "mDefaults", title: "Change Defaults",
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
			}
    	}            
	}
    
    page name: "mDevices"    
        def mDevices(){
            dynamicPage(name: "mDevices", title: "",install: false, uninstall: false) {
                section ("Select devices", hideWhenEmpty: true){ }
                section ("Lights and Switches", hideWhenEmpty: true){  
                    input "cSwitch", "capability.switch", title: "Allow These Switch(es)...", multiple: true, required: false, submitOnChange: true
                    input "cVent", "capability.switchLevel", title: "Allow These Smart Vent(s)...", multiple: true, required: false
                    input "cFan", "capability.switchLevel", title: "Allow These Fan(s)...", multiple: true, required: false
                }
                section ("PIN Protected Devices (Voice Enabled Setting)" , hideWhenEmpty: true) {
                    input "cTstat", "capability.thermostat", title: "Allow These Thermostat(s)...", multiple: true, required: false, submitOnChange: true
                    	if (cTstat) {input "usePIN_T", "bool", title: "Use PIN to control Thermostats?", default: false}
                    input "cDoor", "capability.garageDoorControl", title: "Allow These Garage Door(s)...", multiple: true, required: false, submitOnChange: true
                    	if (cDoor) {input "usePIN_D", "bool", title: "Use PIN to control Doors?", default: false}  
                    input "cLock", "capability.lock", title: "Allow These Lock(s)...", multiple: true, required: false, submitOnChange: true
                    	if (cLock) {input "usePIN_L", "bool", title: "Use PIN to control Locks?", default: false}
                } 
                section ("Sensors", hideWhenEmpty: true) {
                 	input "cMotion", "capability.motionSensor", title: "Allow These Motion Sensor(s)...", multiple: true, required: false
                    input "cContact", "capability.contactSensor", title: "Allow These Contact Sensor(s)...", multiple: true, required: false      
                    input "cPresence", "capability.presenceSensor", title: "Allow These Presence Sensors(s)...", multiple: true, required: false
                }
                section ("Media" , hideWhenEmpty: true){
                    input "cSpeaker", "capability.musicPlayer", title: "Allow These Media Player Type Device(s)...", required: false, multiple: true
                } 
                section ("Batteries", hideWhenEmpty: true ){
                    input "cBattery", "capability.battery", title: "Allow These Device(s) with Batteries...", required: false, multiple: true
                } 
                section ("Weather Alerts") {
                    input "cWeather", "enum", title: "Choose Weather Alerts...", required: false, multiple: true, submitOnChange: true,
                    options: [
                    "TOR":	"Tornado Warning",
                    "TOW":	"Tornado Watch",
                    "WRN":	"Severe Thunderstorm Warning",
                    "SEW":	"Severe Thunderstorm Watch",
                    "WIN":	"Winter Weather Advisory",
                    "FLO":	"Flood Warning",
                    "WND":	"High Wind Advisoryt",
                    "HEA":	"Heat Advisory",
                    "FOG":	"Dense Fog Advisory",
                    "FIR":	"Fire Weather Advisory",
                    "VOL":	"Volcanic Activity Statement",
                    "HWW":	"Hurricane Wind Warning"
                    ]
                 }
            }
        }   
    page name: "mFeedback"
        def mFeedback() {
            dynamicPage (name: "mFeedback", uninstall: false) {
                section ("Switches and Dimmers", hideWhenEmpty: true) {
                    if (faudioTextOn || faudioTextOff || speech11 || music11) paragraph "Configured with Settings"
                        input "fShowSwitches", "bool", title: "Switches and Dimmers", default: false, submitOnChange: true
                    if (fShowSwitches) {        
                        input "faudioTextOn", "text", title: "Alexa says this...", description: "...when the last event was on", required: false, capitalization: "sentences"
                        input "faudioTextOff", "text", title: "Alexa says this...", description: "...when the last event was off", required: false, capitalization: "sentences"
                        input "speech11", "capability.speechSynthesis", title: "Optional send Alexa Feedback to this Message Player", required: false, multiple: true, submitOnChange: true
                        input "music11", "capability.musicPlayer", title: "Optional send Alexa Feedback to this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                        if (music11) {
                            input "volume11", "number", title: "Temporarily change volume", description: "0-100%", required: false
                            input "resumePlaying11", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                            }
                    }             
                }
                section("Doors and Windows", hideWhenEmpty: true) {
                    if (fAudioTextOpen || fAudioTextClosed || speech12 || music12) paragraph "Configured with Settings"
                    input "fShowContacts", "bool", title: "Doors and Windows", default: false, multiple: false, submitOnChange: true
                    if (fShowContacts) {
                        input "fAudioTextOpen", "text", title: "Alexa says this...", description: "...when the last event was door opened", required: false, capitalization: "sentences"
                        input "fAudioTextClosed", "text", title: "Alexa says this...", description: "...when the last event was door closed", required: false, capitalization: "sentences"
                        input "speech12", "capability.speechSynthesis", title: "Optional send Alexa Feedback to this Message Player", required: false, multiple: true, submitOnChange: true
                        input "music12", "capability.musicPlayer", title: "Optional send Alexa Feedback to this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                        if (music12) {
                            input "volume12", "number", title: "Temporarily change volume", description: "0-100%", required: false
                            input "resumePlaying12", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                            }
                        }
                    }    
                section("Locks", hideWhenEmpty: true) {
                    if (fAudioTextLocked || fAudioTextUnlocked || speech13 || music13) paragraph "Configured with Settings"
                    input "fShowLocks", "bool", title: "Locks", default: false, submitOnChange: true
                    if (fShowLocks) {
                        input "fAudioTextLocked", "text", title: "Alexa says this...", description: "...when the last event was locked", required: false, capitalization: "sentences"
                        input "fAudioTextUnlocked", "text", title: "Alexa says this...", description: "...when the last event was door unlocked", required: false, capitalization: "sentences"
                        input "speech13", "capability.speechSynthesis", title: "Optional send Alexa Feedback to this Message Player", required: false, multiple: true, submitOnChange: true
                        input "music13", "capability.musicPlayer", title: "Optional send Alexa Feedback to this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                        if (music13) {
                            input "volume13", "number", title: "Temporarily change volume", description: "0-100%", required: false
                            input "resumePlaying13", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                            }
                        }
                    }
                section("Motion Sensors", hideWhenEmpty: true) {
                    if (fAudioTextActive || fAudioTextInactive || speech14 || music14) paragraph "Configured with Settings"
                    input "fShowMotion", "bool", title: "Motion Sensors", default: false,  submitOnChange: true
                    if (fShowMotion) {
                        input "fAudioTextActive", "text", title: "Alexa says this...", description: "...when the last motion event was active", required: false, capitalization: "sentences"
                        input "fAudioTextInactive", "text", title: "Alexa says this...", description: "...when the last motion event was inactive", required: false, capitalization: "sentences"
                        input "speech14", "capability.speechSynthesis", title: "Optional send Alexa Feedback to this Message Player", required: false, multiple: true, submitOnChange: true
                        input "music14", "capability.musicPlayer", title: "Optional send Alexa Feedback to this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                        if (music14) {
                            input "volume14", "number", title: "Temporarily change volume", description: "0-100%", required: false
                            input "resumePlaying14", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                            }
                        }
                    }        
                section("Presence Sensors", hideWhenEmpty: true) {
                    if (fAudioTextPresent || fAudioTextNotPresent || speech15 || music15) paragraph "Configured with Settings"
                    input "fShowPresence", "bool", title: "Presence Sensors", default: false, submitOnChange: true
                    if (fShowPresence) {
                        input "fAudioTextPresent", "text", title: "Alexa says this...", description: "...when the last event was arrived", required: false, capitalization: "sentences"
                        input "fAudioTextNotPresent", "text", title: "Alexa says this...", description: "...when the last event was not home", required: false, capitalization: "sentences"
                        input "speech15", "capability.speechSynthesis", title: "Optional send Alexa Feedback to this Message Player", required: false, multiple: true, submitOnChange: true
                        input "music15", "capability.musicPlayer", title: "Optional send Alexa Feedback to this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                        if (music15) {
                            input "volume15", "number", title: "Temporarily change volume", description: "0-100%", required: false
                            input "resumePlaying15", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                            }
                        }
                    }   
                 section("Thermostats", hideWhenEmpty: true) {
                    if (fAudioTextHeating || fAudioTextCooling || speech18 || music18) paragraph "Configured with Settings"
                        input "fShowTstat", "bool", title: "Thermostats", default: false, submitOnChange: true
                    if (fShowTstat) {
                        input "fAudioTextHeating", "text", title: "Alexa says this...", description: "Message to play when the Heating Set Point Changes", required: false, capitalization: "sentences"
                        input "fAudioTextCooling", "text", title: "Alexa says this...", description: "Message to play when the Cooling Set Point Changes", required: false, capitalization: "sentences" 
                        input "speech18", "capability.speechSynthesis", title: "Optional send Alexa Feedback to this Message Player", required: false, multiple: true, submitOnChange: true
                        input "music18", "capability.musicPlayer", title: "Optional send Alexa Feedback to this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                        if (music18) {
                            input "volume18", "number", title: "Temporarily change volume", description: "0-100%", required: false
                            input "resumePlaying18", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                        }
                    }
                 }		
                 section("Weather", hideWhenEmpty: true) {
                    if (fAudioTextWeather || speech19 || music19) paragraph "Configured with Settings"
                        input "fShowWeather", "bool", title: "Weather Alerts", default: false, submitOnChange: true
                    if (fShowWeather) {
                        input "fAudioTextWeather", "text", title: "Alexa says this...", description: "When a Weather Alert is in effect", required: false, capitalization: "sentences"
                        input "speech19", "capability.speechSynthesis", title: "Optional send Alexa Feedback to this Message Player", required: false, multiple: true, submitOnChange: true
                        input "music19", "capability.musicPlayer", title: "Optional send Alexa Feedback to this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                        if (music19) {
                            input "volume19", "number", title: "Temporarily change volume", description: "0-100%", required: false
                            input "resumePlaying19", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                        }
                    }		
                } 
            }
        }
    
    page name: "mDefaults"
        def mDefaults(){
                dynamicPage(name: "mDefaults", title: "", uninstall: false){
                    section ("General Defaults") {            
                        input "cLevel", "number", title: "Alexa Adjusts Light Levels by using a scale of 1-10 (default is +/-3)", defaultValue: 3, required: false
                        input "cVolLevel", "number", title: "Alexa Adjusts the Volume Level by using a scale of 1-10 (default is +/-2)", defaultValue: 3, required: false
                        input "cTemperature", "number", title: "Alexa Automatically Adjusts temperature by using a scale of 1-10 (default is +/-1)", defaultValue: 1, required: false
                    }
                     section ("Fan Control") {            
                        input "cHigh", "number", title: "Alexa Adjusts High Level to 99% by default", defaultValue: 99, required: false
                        input "cMedium", "number", title: "Alexa Adjusts Medium Level to 66% by default", defaultValue: 66, required: false
                        input "cLow", "number", title: "Alexa Adjusts Low Level to 33% by default", defaultValue: 33, required: false
                        input "cFanLevel", "number", title: "Alexa Automatically Adjusts Ceiling Fans by using a scale of 1-100 (default is +/-33%)", defaultValue: 33, required: false
                     }
                     section ("Security") {  
                        input "cPIN", "password", title: "Set a PIN number to prevent unathorized use of Voice Control", default: false, required: false
                    }
                }
        }
page name: "mProfiles"    
    def mProfiles() {
        dynamicPage (name: "mProfiles", title: "", install: true, uninstall: false) {
        	if (childApps.size()) { 
            	section(childApps.size()==1 ? "One Room configured" : childApps.size() + " Room configured" )
            }
            if (childApps.size()) {  
            	section("Rooms",  uninstall: false){
                	app(name: "rooms", appName: "Rooms", namespace: "EchoLabs", title: "Create a new Room", multiple: true,  uninstall: false)
            	}
            }
            else {
            	section("Rooms",  uninstall: false){
            		paragraph "NOTE: Looks like you haven't created any Rooms yet.\n \nPlease make sure you have installed the Rooms Smart App Add-on before creating a new Room!"
            		app(name: "room", appName: "Rooms", namespace: "EchoLabs", title: "Create a new Room", multiple: true,  uninstall: false)
        		}
            }
       }
}
page name: "mSettings"  
	def mSettings(){
        dynamicPage(name: "mSettings", uninstall: true) {
 			section ("Directions, How-to's, and Troubleshooting") { 
 				href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant", title: "EchoSistant Wiki", description: none
            input "debug", "bool", title: "Enable Debug Logging", default: false, submitOnChange: true 
            }
            section ("Amazon AWS Skill Details") { 
				href "mSkill", title: "Tap to view setup data for the AWS Main Intent Skill...", description: ""
            }                
            section ("Application ID and Token") {
            	input "showTokens", "bool", title: "Show IDs", default: false, submitOnChange: true
            		if (showTokens) paragraph "The Security Tokens are now displayed in the Live Logs section of the IDE"
    				if (showTokens) log.info "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
            		if (showTokens) paragraph 	"Access token:\n"+
                                       			"${state.accessToken}\n"+
                                        		"Application ID:\n"+
                                        		"${app.id}"
            
             	href "mTokens", title: "Revoke/Reset Security Access Token", description: none
             	def msg = state.accessToken != null ? state.accessToken : 	"Could not create Access Token. OAuth may not be enabled. "+
             																"Go to the SmartApp IDE settings to enable OAuth."            
            
            }
             section("Tap below to remove the ${textAppName()} application.  This will remove ALL Profiles and the App from the SmartThings mobile App."){
             }	
    	}	            	
	}

page name: "mSkill"
    def mSkill(){
            dynamicPage(name: "mSkill", uninstall: false) {
 			section ("List of Profiles") { 
				href "mProfileDetails", title: "View your List of Profiles for copy & paste to the AWS Skill...", description: "", state: "complete" 
            }
            section ("List of Devices") {
				href "mDeviceDetails", title: "View your List of Devices for copy & paste to the AWS Skill...", description: "", state: "complete" 
				}
            }
        }
    
    page name: "mProfileDetails"
        def mProfileDetails(){
                dynamicPage(name: "mProfileDetails", uninstall: false) {
                section ("LIST_OF_PROFILES") { 
                    def ProfileList = getProfileDetails()   
                        paragraph ("${ProfileList}")
                        log.info "\nLIST_OF_PROFILES \n${ProfileList}"
                            }
                        }
                    } 
    
    page name: "mDeviceDetails"
        def mDeviceDetails(){
                dynamicPage(name: "mDeviceDetails", uninstall: false) {
                section ("LIST_OF_DEVICES") { 
                    def DeviceList = getDeviceDetails()
                        paragraph ("${DeviceList}")
                        log.info "\nLIST_OF_DEVICES \n${DeviceList}"
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
                                    revokeAccessToken()
                                    state.accessToken = null
                                    OAuthToken()
                                    def msg = state.accessToken != null ? "New access token:\n${state.accessToken}\n\n" : "Could not reset Access Token."+
                                    "OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
                                    paragraph "${msg}"
                                    paragraph "The new access token and app ID are now displayed in the Live Logs tab of the IDE."
                                    log.info "New IDs: STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                                }
                                section(" "){ 
                                    href "mainParentPage", title: "Tap Here To Go Back To Main Menu", description: none 
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
	path("/b") { action: [GET: "processBegin"] }
	path("/c") { action: [GET: "controlDevices"] }
	path("/f") { action: [GET: "feedbackHandler"] }
    path("/p") { action: [GET: "controlProfiles"] }
	path("/t") { action: [GET: "processTts"] }
}
/************************************************************************************************************
		Base Process
************************************************************************************************************/
def installed() {
	if (debug) log.debug "Installed with settings: ${settings}"
}
def updated() { 
	if (debug) log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}
def initialize() {
        sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
    	state.lastMessage = null
		state.lastIntent  = null
    	state.lastTime  = null
        state.lambdaReleaseTxt = "Not Set"
        state.lambdaReleaseDt = "Not Set" 
		state.lambdatextVersion = "Not Set"
    	state.weatherAlert = "There are no weather alerts for your area"
        state.disableConv = false
        state.pContCmds = true
        state.pContCmdsR = true
		state.usePIN_T = false
		state.usePIN_L = false
		state.usePIN_D = false
        state.savedPINdata
        def children = getChildApps()
    	if (debug) log.debug "Refreshing Profiles for CoRE, ${getChildApps()*.label}"
		if (!state.accessToken) {
        	if (debug) log.error "Access token not defined. Attempting to refresh. Ensure OAuth is enabled in the SmartThings IDE."
                OAuthToken()
			}
	}
/************************************************************************************************************
		CoRE Integration
************************************************************************************************************/
def getProfileList(){
		return getChildApps()*.label
		if (debug) log.debug "Refreshing Profiles for CoRE, ${getChildApps()*.label}"
}
def childUninstalled() {
	if (debug) log.debug "Profile has been deleted, refreshing Profiles for CoRE, ${getChildApps()*.label}"
    sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
}
/************************************************************************************************************
		Begining Process - Lambda via page b
************************************************************************************************************/
def processBegin(){
    if (debug) log.debug "^^^^____Initial Commands Received from Lambda___^^^^"
    
    def versionTxt  = params.versionTxt 		
    def versionDate = params.versionDate
    def releaseTxt = params.releaseTxt
        state.lambdaReleaseTxt = releaseTxt
        state.lambdaReleaseDt = versionDate
        state.lambdatextVersion = versionTxt
        
    def versionSTtxt = textVersion()
	def pContinue
    if (state.disableConv) {pContinue = state.disableConv}
    
    if (debug){
        log.debug "Message received from Lambda with: (ver) = '${versionTxt}', (date) = '${versionDate}', (release) = '${releaseTxt}'"+ 
        ". And sent to Lambda: pContinue = '${pContinue}', versionSTtxt = '${versionSTtxt}'"
	}
    	return ["pContinue":pContinue, "versionSTtxt":versionSTtxt]
}   
/************************************************************************************************************
   CONTROL DEVICES - from Lambda via page c
************************************************************************************************************/
def controlDevices() {
		def ctCommand = params.cCommand
        def ctNum = params.cNum
        def ctPIN = params.cPIN
        def ctDevice = params.cDevice
        def ctUnit = params.cUnit
        def ctGroup = params.cGroup       
		def pintentName = params.intentName

        def outputTxt = "I wish I could help, but EchoSistant couldn't find the device or the command is not supported, TOP"
        def pTryAgain = false
		def pPIN = false
        def deviceType = " "
        def command = " "
		def numText = " "
        def result = " "
        def delay = false
        def data
    if (debug) log.debug	"Received Lambda request to control devices with settings:" +
        					  	" (ctCommand)= ${ctCommand}',(ctNum) = '${ctNum}', (ctPIN) = '${ctPIN}', (ctDevice) = '${ctDevice}', (ctUnit) = '${ctUnit}', (ctGroup) = '${ctGroup}', (pintentName) = '${pintentName}'"   
        
    if (ctNum == "undefined" || ctNum =="?") {ctNum = 0 as int } 

    if (pintentName == "main") {
        if (ctCommand == "repeat") {
            if (debug) log.debug "Processing repeat last message delivered to any of the Profiles"
                outputTxt = getLastMessageMain()
                if (debug) log.debug "Received message: '${outputTxt}' ; sending to Lambda with pContCmds = '${state.pContCmds}' and  pContCmdsR = '${state.pContCmdsR}'"
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
        }
        else {
            if (ctCommand == "cancel") {
                if (debug) log.debug "Canceling timmer!"
                    unschedule()
                    outputTxt = "Ok, canceling timer"
                if (debug) log.debug "Cancel message received; sending '${outputTxt}' to Lambda with pContCmds = '${state.pContCmds}' and  pContCmdsR = '${state.pContCmdsR}'" 
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
            }
            if (ctCommand == "stop" && ctUnit == "conversation") {
                if (debug) log.debug "Stopping Continuation Responses!"
                    state.disableConv = true
                    state.pContCmds = false
                    state.pContCmdsR = false
                    outputTxt = "Really? So, you don't like me to talk back to you. That's ok, if you change your mind, just say, start conversation"
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
            }
            if (ctCommand == "start" && ctUnit == "conversation") {
                if (debug) log.debug "Starting Continuation Responses!"
                    state.disableConv = true
                    state.pContCmds = true
                    state.pContCmdsR = true
                    outputTxt = "Great! I will talk back to you every time you give me a new command. If you get tired of me promting for more commands, just say stop the conversation"
                	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
            }
             if (ctGroup != "undefined" && ctUnit != "undefined") {    
                if (ctCommand == "enable" || ctCommand == "activate") {
                    if (debug) log.debug "Activating PIN"
                    if (ctGroup == "thermostats"  && ctUnit == "pin number") {state.usePIN_T = true}
                    else if (ctGroup == "locks"  && ctUnit == "pin number") {state.usePIN_L = true}
                    else if (ctGroup == "doors"  && ctUnit == "pin number") {state.usePIN_D = true}
                    else if (ctUnit == "pin number"){
                    	if (debug) log.debug "usePIN_T = '${state.usePIN_T}', usePIN_L = '${state.usePIN_L}', usePIN_D = '${state.usePIN_D}' "
                    	pPIN = true
                    	outputTxt = "Ok, the pin has been activated for " + ctGroup + ".  To disable, just say disable the PIN for " + ctGroup
                    	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]          
                	}
                }
                if (ctCommand == "disable" || ctCommand == "deactivate") {
                    if (ctGroup == "thermostats"  && ctUnit == "pin number") {state.usePIN_T = false}
                    else if (ctGroup == "locks"  && ctUnit == "pin number") {state.usePIN_L = false}
                    else if (ctGroup == "doors"  && ctUnit == "pin number") {state.usePIN_D = false}
                    else if (ctUnit == "pin"){
                    	if (debug) log.debug "usePIN_T = '${state.usePIN_T}', usePIN_L = '${state.usePIN_L}', usePIN_D = '${state.usePIN_D}'"
                    	outputTxt = "Ok, I will no longer ask you for the pin number when controlling the " + ctGroup + ".  If you want to enable, just say enable the PIN for " + ctGroup
						return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                }
            }
            else {
                if (ctNum > 0 && ctDevice != "undefined" && ctCommand == "undefined") {
            		ctCommand = "set"
                }
                if (ctPIN == cPIN) {
					data = state.savedPINdata
                	if (debug) log.debug "PIN Matched! Preparing data: '${data}' "
                	outputTxt = controlHandler(data)
					return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                }
                if (ctPIN != cPIN && ctPIN != "undefined" ) {
                	if (debug) log.debug "PIN NOT Matched! PIN = '${cPIN}', ctPIN= '${ctPIN}', ctCommand ='${ctCommand}' "
                }
                if (ctCommand != "undefined") {
                	if (debug) log.debug "Fetching command and device type"           
                    def  getCMD = getCommand(ctCommand, ctUnit) 
                    deviceType = getCMD.deviceType
                    command = getCMD.command
                    if (debug) log.debug "Received command data: deviceType= '${deviceType}', command= '${command}', STARTING MAIN PROCESS"
                }
            }
        }
    // Units/Text Conversions	
        if (ctUnit =="?") {ctUnit = "undefined"}
        if (ctUnit == "minute" || ctUnit == "minutes" || ctUnit.contains ("minutes") || ctUnit.contains ("minute")) {
            ctUnit = "minutes"
            numText = ctNum == 1 ? ctNum + " minute" : ctNum + " minutes" 
        }      
        else if (ctUnit == "degrees") {
        	int ctN = ctNum
            numText = ctN + " degrees"
        }
        else if (ctUnit == "percent") {
            numText = ctNum + " percent"    
        }
        else if (ctUnit != "degrees" || ctUnit != "minutes") {numText = "by " + cLevel*10 + " percent"}       

        if (deviceType == "temp") {def numTxtTMP = cTemperature == 1 ? cTemperature + " degree" : cTemperature + " degrees" }
    // Units/Text Conversions Ends 
        
        if (deviceType == "light" || deviceType == "general" ) {
            if (cSwitch) {
                if (debug) log.debug "Searching for a switch named '${ctDevice}'"
                def deviceMatch = cSwitch.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}             
                if (deviceMatch) {
                    if (debug) log.debug "Found a device: '${deviceMatch}'"
                    device = deviceMatch
                    if (ctNum > 0 && ctUnit == "minutes") {
                        device = device.label
                        delay = true
                        data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                        runIn(ctNum*60, controlHandler, [data: data])
                        if (command == "on" || command == "off" ) {outputTxt = "Ok, turning " + ctDevice + " " + command + ", in " + numText}
                        else if (command == "decrease") {outputTxt = "Ok, decreasing the " + ctDevice + " level in " + numText}
                        else if (command == "increase") {outputTxt = "Ok, increasing the " + ctDevice + " level in " + numText}
						return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                    else {
                        delay = false
                        data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                        outputTxt = controlHandler(data)
						return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                }
                // a catch all phrase for general commands or light commands with no matching device 
                if (state.pContCmds == true || state.pContCmds == true) {
        			pTryAgain = false
        		}
        		else pTryAgain = true
                outputTxt = "I wish I could help, but EchoSistant couldn't find the device or the command is not supported yet, "
            }
        }
        else if (deviceType == "temp") {
                if (cTstat) {           
                    if (debug) log.debug "Searching for a thermostat named '${ctDevice}'"
                    def deviceMatch = cTstat.find {t -> t.label.toLowerCase() == ctDevice.toLowerCase()}
                    if (deviceMatch) {
 						device = deviceMatch 
                        if (debug) log.debug "Found a device: '${deviceMatch}'"
                                 	
                        if(state.usePIN_T == true) {
            				if (debug) log.debug "PIN protected device type - '${deviceType}'"
                			delay = true
                            data = [type: "cTstat", command: command, device: device, unit: ctUnit, num: ctNum]
                			//state.
                            state.savedPINdata = evt.jsonData && evt.jsonData?.data ? evt.jsonData.data : []
                            def savedPINdata = data
                			outputTxt = "Pin number please"
                			pPIN = true
                			return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
            			}
                        else {
                       
                            if (ctNum && ctUnit == "minutes") {
                                delay = true
                                data = [type: "cTstat", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                runIn(ctNum*60, delayHandler, [data: data])
                                if (command == "decrease") {outputTxt = "Ok, decreasing the " + ctDevice + " temperature in " + numText}
                                else if (command == "increase") {outputTxt = "Ok, increasing the " + ctDevice + " temperature in " + numText}
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                            }
                            else {
                            	delay = false
                            	data = [type: "cTstat", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                            	outputTxt = controlHandler(data)
                            	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                            }
                        }
                   }
                }
         }
        else if (deviceType == "lock") {
            if (cLock) {   
                if (debug) log.debug "Searching for a lock named '${ctDevice}'"
                def deviceMatch = cLock.find {l -> l.label.toLowerCase() == ctDevice.toLowerCase()}             
                if (deviceMatch) {
                    if (debug) log.debug "Found a device: '${deviceMatch}'"
                    device = deviceMatch
                    if (ctNum > 0 && ctUnit == "minutes") {
                        device = device.label
                        delay = true
                        data = [type: "cLock", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                        runIn(ctNum*60, controlHandler, [data: data])
                        if (command == "lock") {outputTxt = "Ok, locking the " + ctDevice + " in " + numText}
                        else if (command == "unlock") {outputTxt = "Ok, unlocking the " + ctDevice + " in " + numText}
						return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                    else {
                        delay = false
                        data = [type: "cLock", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                        outputTxt = controlHandler(data)
						return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                }
            }
        }
        else if (deviceType == "volume") {      
            if (debug) log.debug "Searching for a volume control device named '${ctDevice}'"     
                if (cSpeaker) {
                    def deviceMatch = cSpeaker.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}
                    if (deviceMatch) {
                        if (debug) log.debug "Found a device: '${deviceMatch}'"
                        device = deviceMatch
                        delay = false
                        data = [type: "cVolume", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                        outputTxt = controlHandler(data)
						return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                    }
                }  
        }
        else if (deviceType == "fan") {
            if (cFan) {     
                if (debug) log.debug "Searching for a fan named '${ctDevice}'"
                def deviceMatch = cFan.find {f -> f.label.toLowerCase() == ctDevice.toLowerCase()}
                if (deviceMatch) {
                    if (debug) log.debug "Found a device: '${deviceMatch}'"
                        device = deviceMatch
                        if (ctNum && ctUnit == "minutes") {
                            delay = true
                            data = [type: "cFan", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                            runIn(ctNum*60, delayHandler, [data: data])
                            if (command == "decrease") {outputTxt = "Ok, decreasing the " + ctDevice + " temperature in " + numText}
                            else if (command == "increase") {outputTxt = "Ok, increasing the " + ctDevice + " temperature in " + numText}
							return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }
                        else {
                            delay = false
                            data = [type: "cFan", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                            outputTxt = controlHandler(data)
							return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }
                }
            }
        }
        else if (deviceType == "door") {
            if (cDoor) {          
                if (debug) log.debug "Searching for a door named '${ctDevice}'"
                def deviceMatch = cDoor.find {d -> d.label.toLowerCase() == ctDevice.toLowerCase()}
                if (deviceMatch) {
                    if (debug) log.debug "Found a device: '${deviceMatch}'"
                        device = deviceMatch
                        if (ctNum && ctUnit == "minutes") {
                            delay = true
                            data = [type: "cDoor", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                            runIn(ctNum*60, delayHandler, [data: data])
                            if (command == "open") {outputTxt = "Ok, opening " + ctDevice + " in " + numText}
                            else if (command == "close") {outputTxt = "Ok, closing " + ctDevice + " in " + numText}
							return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }
                        else {
                            delay = false
                            data = [type: "cDoor", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                            outputTxt = controlHandler(data)
							return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                        }
                }
                else {
                    //this is needed for Garage Doors that are set up as relays
                    if (cSwitch) {
                    if (debug) log.debug "Searching for a relay named '${ctDevice}'"
                        deviceMatch = cSwitch.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}             
                        if (deviceMatch) {
                            if (debug) log.debug "Found a device: '${deviceMatch}'"
                                command = "onD"
                                device = deviceMatch
                                if (ctNum > 0 && ctUnit == "minutes") {
                                    device = device.label
                                    delay = true
                                    data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                    runIn(ctNum*60, controlHandler, [data: data])
                                    if (ctCommand == "open") {outputTxt = "Ok, opening the " + ctDevice + " in " + numText}
                                    else if (command == "close") {outputTxt = "Ok, closing the " + ctDevice + " in " + numText}
								return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                                }
                                else {
                                    delay = false
                                    data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                    controlHandler(data)
                                    if (ctCommand == "open") {outputTxt = "Ok, opening the " + ctDevice}
                                    else if (ctCommand == "close") {outputTxt = "Ok, closing the " + ctDevice}
								return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                                }
                       }
                    }
                    else if (cVents) {
                        if (debug) log.debug "Searching for a vent named '${ctDevice}'"
                        deviceMatch = cVents.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}             
                        if (deviceMatch) {
                            if (debug) log.debug "Found a device: '${deviceMatch}'"
                                if (command == "open") {command = "onD"}
                                if (command == "close") {command = "offD"}
                                device = deviceMatch
                                if (ctNum > 0 && ctUnit == "minutes") {
                                    device = device.label
                                    delay = true
                                    data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                    runIn(ctNum*60, controlHandler, [data: data])
                                    if (ctCommand == "open") {outputTxt = "Ok, opening the " + ctDevice + " in " + numText}
                                    else if (command == "close") {outputTxt = "Ok, closing the " + ctDevice + " in " + numText}
								return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                                }
                                else {
                                    delay = false
                                    data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                    controlHandler(data)
                                    if (ctCommand == "open") {outputTxt = "Ok, opening the " + ctDevice}
                                    else if (ctCommand == "close") {outputTxt = "Ok, closing the " + ctDevice}
								return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
                                }
                       }
                    }
                }
            }
        }
        if (debug) log.debug "Sending response to Alexa with settings BOTTOM: pContCmds = ${state.pContCmds}, pContCmdsR = ${state.pContCmdsR}, message:'${outputTxt}'"               
		/*
        if (state.pContCmds == true || state.pContCmds == true) {
        	pTryAgain = false
        }
        else pTryAgain = true
        	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
        */
        
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pContCmdsR":state.pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
   }
}

def getPIN(data){
    def deviceType = data.type
    def deviceCommand = data.command
   	def deviceD = data.device
    def unitU = data.unit
    def numN = data.num
    def delayD = data.delay
	def result = " "
        if (debug) log.debug 	"Received PIN Protected data with settings: " +
        					" (deviceType)= ${deviceType}',(deviceCommand) = '${deviceCommand}', (deviceD) = '${deviceD}', " +
                            "(unitU) = '${unitU}', (numN) = '${numN}', (delayD) = '${delayD}'" 
                            
}
    

/************************************************************************************************************
   DEVICE CONTROL HANDLER
************************************************************************************************************/      
def controlHandler(data) {   
    def deviceType = data.type
    def deviceCommand = data.command
   	def deviceD = data.device
    def unitU = data.unit
    def numN = data.num
    def delayD = data.delay
	def result = " "

    if (debug) log.debug 	"Received device control handler data: " +
        					" (deviceType)= ${deviceType}',(deviceCommand) = '${deviceCommand}', (deviceD) = '${deviceD}', " +
                            "(unitU) = '${unitU}', (numN) = '${numN}', (delayD) = '${delayD}'"  

    if (deviceType == "cSwitch") {
    	if (deviceCommand == "on" || deviceCommand == "off") {
            if (delayD == false) {
                deviceD."${deviceCommand}"()
            	result = "Ok, turning " + deviceD + " " + deviceCommand 
                return result
            }
            else {
            	deviceD = cSwitch.find {s -> s.label == deviceD}   
            	deviceD."${deviceCommand}"()
            }  
        }
        else if (deviceCommand == "onD") {
        		deviceD.on()
        }
        else if (deviceCommand == "offD") {
        		deviceD.off()
        }
        else if (deviceCommand == "increase" || deviceCommand == "decrease" || deviceCommand == "setLevel") {
 			if (delayD == true) {  
                deviceD = cSwitch.find {s -> s.label == deviceD}   
            }
            def currLevel = deviceD.latestValue("level")
            def currState = deviceD.latestValue("switch")
            def newLevel = cLevel*10
			if (unitU == "percent") newLevel = numN      
            if (deviceCommand == "increase") {
            	if (unitU == "percent") {
                	newLevel = numN
                }   
                else {
                	newLevel =  currLevel + newLevel
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
            	}
            }
            if (deviceCommand == "decrease") {
            	if (unitU == "percent") {
                	newLevel = numN
                }   
                else {
                	newLevel =  currLevel - newLevel
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                }            
            }
            if (deviceCommand == "setLevel") {
            	if (unitU == "percent") {
                	newLevel = numN
                }   
                else {
                	newLevel =  numN*10
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                }            
            }
            if (newLevel > 0 && currState == "off") {
            	deviceD.on()
            	deviceD.setLevel(newLevel)
            }
            else {                                    
            	if (newLevel == 0 && currState == "on") {deviceD.off()}
                else {deviceD.setLevel(newLevel)}
            } 
            result = "Ok, setting  " + deviceD + " to " + newLevel + " percent"
            if (delayD == false) { return result } 
    	}
	}
	else if (deviceType == "cTstat") {
 		if (delayD == true) {  
                deviceD = cTstat.find {t -> t.label == deviceD}   
        }    
    	def currentMode = deviceD.latestValue("thermostatMode")
    	def currentHSP = deviceD.latestValue("heatingSetpoint") 
        def currentCSP = deviceD.latestValue("coolingSetpoint") 
    	def currentTMP = deviceD.latestValue("temperature") 
    	def newSetPoint = currentTMP           
		numN = numN < 60 ? 60 : numN >85 ? 85 : numN
        if (unitU == "degrees") {
    		newSetPoint = numN
            int cNewSetPoint = newSetPoint
    		if (debug) log.debug "Targeted set point is = '${newSetPoint}', current temperature is = '${currentTMP}'"
    		if (newSetPoint > currentTMP) {
    			if (currentMode == "cool" || currentMode == "off") {
    				deviceD?."heat"()
					if (debug) log.debug 	"Turning heat on because requested temperature of '${newSetPoint}' "+
                    						"is greater than current temperature of '${currentTMP}' " 
    			}
				deviceD?.setHeatingSetpoint(newSetPoint)
				if (debug) log.debug "Adjusting Heating Set Point to '${newSetPoint}' because requested temperature is greater than current temperature of '${currentTMP}'"
                result = "Ok, setting " + deviceD + " heating to " + cNewSetPoint 
            	if (delayD == false) { return result }
            }
 			else if (newSetPoint < currentTMP) {
				if (currentMode == "heat" || currentMode == "off") {
					deviceD?."cool"()
					if (debug) log.debug "Turning AC on because requested temperature of '${newSetPoint}' is less than current temperature of '${currentTMP}' "    
				}
				deviceTMatch?.setCoolingSetpoint(newSetPoint)                 
				if (debug) log.debug "Adjusting Cooling Set Point to '${newSetPoint}' because requested temperature is less than current temperature of '${currentTMP}'"
                result = "Ok, setting " + deviceD + " cooling to " + cNewSetPoint + " degrees "
            		if (delayD == false) { return result }                        
            }
            else result = "Your room temperature is already " + cNewSetPoint + " degrees "
            		if (delayD == false) { return result }                        
            
		}
		if (deviceCommand == "increase") {
			newSetPoint = currentTMP + cTemperature
			newSetPoint = newSetPoint < 60 ? 60 : newSetPoint >85 ? 85 : newSetPoint
            int cNewSetPoint = newSetPoint
			if (currentMode == "cool" || currentMode == "off") {
				deviceD?."heat"()
				if (debug) log.debug "Turning heat on because requested command asked for heat to be set to '${newSetPoint}'" 
			}
			else {
				if  (currentHSP < newSetPoint) {
					deviceD?.setHeatingSetpoint(newSetPoint)
					thermostat?.poll()
					if (debug) log.debug "Adjusting Heating Set Point to '${newSetPoint}'"
                    result = "Ok, setting " + deviceD + " heating to " + cNewSetPoint + " degrees "
            		if (delayD == false) { return result }     
                }
                else {
                   	if (debug) log.debug "Not taking action because heating is already set to '${currentHSP}', which is higher than '${newSetPoint}'" 
                    result = "Your heating set point is already higher than  " + cNewSetPoint + " degrees "
            		if (delayD == false) { return result }     
               	}  
            }
       	}
        if (deviceCommand == "decrease") {
        	newSetPoint = currentTMP - cTemperature
        	newSetPoint = newSetPoint < 60 ? 60 : newSetPoint >85 ? 85 : newSetPoint     
            int cNewSetPoint = newSetPoint
            if (currentMode == "heat" || currentMode == "off") {
        		deviceD?."cool"()
        		if (debug) log.debug "Turning AC on because requested command asked for cooling to be set to '${newSetPoint}'"     
        	}   	
        	else {
        		if (currentCSP > newSetPoint) {
        			deviceD?.setCoolingSetpoint(newSetPoint)
        			thermostat?.poll()
        			if (debug) log.debug "Adjusting Cooling Set Point to '${newSetPoint}'"
        			result = "Ok, setting " + deviceD + " cooling to " + cNewSetPoint + " degrees "
            		if (delayD == false) { return result } 
                }
        		else {
        			if (debug) log.debug "Not taking action because cooling is already set to '${currentCSP}', which is lower than '${newSetPoint}'"  
                    result = "Your cooling set point is already lower than  " + cNewSetPoint + " degrees "
            		if (delayD == false) { return result }
                } 
        	}  
        }
    }
	else if (deviceType == "cLock") {
   		if (deviceCommand == "lock" || deviceCommand == "unlock") {
            if (delayD == false) {
                deviceD."${deviceCommand}"()
            	if (deviceCommand == "lock") result = "Ok, locking " + deviceD
                else if (deviceCommand == "unlock") result = "Ok, unlocking the  " + deviceD
                return result
            }
            else {
            	deviceD = cLock.find {l -> l.label == deviceD}   
            	deviceD."${deviceCommand}"()
            }  
        }     
    }
	else if (deviceType == "cVolume") {
   		if (deviceCommand == "increase" || deviceCommand == "decrease" || deviceCommand == "setLevel") {
            def currLevel = deviceD.latestValue("level")
            def currState = deviceD.latestValue("switch")
            if (cVolLevel == null) {cVolLevel = 2}
            def newLevel = cVolLevel*10
			if (unitU == "percent") newLevel = numN      
            if (deviceCommand == "increase") {
            	if (unitU == "percent") {
                	newLevel = numN
                }   
                else {
                	newLevel =  currLevel + newLevel
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
            	}
            }
            if (deviceCommand == "decrease") {
            	if (unitU == "percent") {
                	newLevel = numN
                }   
                else {
                	newLevel =  currLevel - newLevel
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                }            
            }
            if (deviceCommand == "setLevel") {
            	if (unitU == "percent") {
                	newLevel = numN
                }   
                else {
                	newLevel =  numN*10
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                }            
            }
            if (newLevel > 0 && currState == "off") {
            	deviceD.on()
            	deviceD.setLevel(newLevel)
            }
            else {                                    
            	if (newLevel == 0 && currState == "on") {deviceD.off()}
                else {deviceD.setLevel(newLevel)}
            } 
            result = "Ok, setting  " + deviceD + " volume to " + newLevel + " percent"
            return result
    	}
    	else {
        	deviceD."${deviceCommand}"()
    		result = "Ok, adjusting the volume of your " + deviceD
            return result
       }
    }
	else if (deviceType == "cDoor") {
            if (delayD == false) {
                deviceD."${deviceCommand}"()
            	if (deviceCommand == "open") result = "Ok, opening " + deviceD
                else if (deviceCommand == "close") result = "Ok, closing " + deviceD
                return result
            }
            else {
            	deviceD = cDoor.find {v -> v.label == deviceD}   
            	deviceD."${deviceCommand}"()
            }  
    }               
    else if (deviceType == "cFan") {
		if (cHigh == null) cHigh = 99
		if (cMedium == null) cMedium = 66
        if (cLow == null) cLow = 33
        if (cFanLevel == null) cFanLevel = 33
		if (delayD == true) {  
        	deviceD = cFan.find {f -> f.label == deviceD}   
        }
		def currLevel = deviceD.latestValue("level")
		def currState = deviceD.latestValue("switch")
		def newLevel = cFanLevel     
        	if (deviceCommand == "increase") {
            	newLevel =  currLevel + newLevel
            	newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                result = "Ok, increasing  " + deviceD + " to " + newLevel + " percent"
       				if (delayD == false) { return result }
            }
            else if (deviceCommand == "decrease") {
               	newLevel =  currLevel - newLevel
            	newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                result = "Ok, decreasing  " + deviceD + " to " + newLevel + " percent"
       				if (delayD == false) { return result }        
            }
            else {
                if (deviceCommand == "high") {newLevel = cHigh}
                if (deviceCommand == "medium") {newLevel = cMedium}
                if (deviceCommand == "low") {newLevel = cLow}
                    deviceD.setLevel(newLevel)
                    result = "Ok, setting  " + deviceD + " to " + newLevel + " percent"
                        if (delayD == false) { return result } 
           }
	}
}
/***********************************************************************************************************************
    LAST MESSAGE HANDLER
***********************************************************************************************************************/
def getLastMessageMain() {
	def outputTxt = "The last message sent was," + state.lastMessage + ", and it was sent to, " + state.lastIntent + ", at, " + state.lastTime
    return  outputTxt 
  	if (debug) log.debug "Sending last message to Lambda ${outputTxt} "
}
/***********************************************************************************************************************
 		SKILL DETAILS
 ***********************************************************************************************************************/
private getProfileDetails() {
	def c = "" 
	def children = getChildApps()	
    	children?.each { child -> 
			c +=child.label +"\n" } 
	def ProfileDetails = "${c}" 
    	return  ProfileDetails
}

private getDeviceDetails() {
    def s = "" 
        cSwitch.each { device -> 
            s +=device.label +"\n" }
    def t = "" 
        cTstat.each { device -> 
            t +=device.label +"\n" }
    def l = "" 
        cLock.each { device -> 
            l +=device.label +"\n" }        
    def m = "" 
        cMotion.each { device -> 
            m +=device.label +"\n" }   
    def c = "" 
        cContact.each { device -> 
            c +=device.label +"\n" } 
    def p = "" 
        cPresence.each { device -> 
            p +=device.label +"\n" } 
    def d = "" 
        cDoor.each { device -> 
            d +=device.label +"\n" } 
    def sp = "" 
        cSpeaker.each { device -> 
            sp +=device.label +"\n" } 
    def v = "" 
        cVent.each { device -> 
            v +=device.label +"\n" }
    def f = "" 
        cFan.each { device -> 
            f +=device.label +"\n" } 
	def DeviceDetails = "${s}"+"${t}"+"${l}"+"${m}"+"${c}"+ "${p}"+"${d}"+"${v}"+"${f}"+"${sp}" 
    	return DeviceDetails
}
/***********************************************************************************************************************
    COMMANDS HANDLER
***********************************************************************************************************************/
private getCommand(command, unit) {
	def deviceType = " "
		if (command) {
	//case "General Commands":
    		deviceType = "general"
        if (unit == "undefined") {
            if (command == "decrease" || command == "down") {
                command = "decrease" 
                deviceType = "general"
            }
            if (command == "increase" || command == "up") {
                command = "increase"
                deviceType = "general"
            }
            if (command == "set" || command == "set level") {
                command = "setLevel"
                deviceType = "general"
            }
        }
	//case "Temperature Commands":  
        if (command == "colder" || command =="not cold enough" || command =="too hot" || command == "too warm") {
            command = "decrease"
            deviceType = "temp"
        }     
        else if (command == "freezing" || command =="not hot enough" || command == "too chilly" || command == "too cold" || command == "warmer") {
            command = "increase"
            deviceType = "temp"
        }
        else if (unit == "degrees" || unit =="heat" || unit =="AC" || unit =="cooling" || unit =="heating") {
            if (command == "up") {
           		command = "increase"
        	}
            else if (command == "down") {
            	command = "decrease"
            }
            deviceType = "temp"
        }
        else if (unit.contains("degrees") ||  unit.contains("heat") ||  unit.contains("AC")) {
           deviceType = "temp"    
       }       
    //case "Dimmer Commands":
        if (command == "darker" || command == "too bright" || command == "dim" || command == "dimmer") {
            command = "decrease" 
            deviceType = "light"
        }
        else if  (command == "not bright enough" || command == "brighter" || command == "too dark" || command == "brighten") {
            command = "increase" 
            deviceType = "light"     
        } 
        else if (unit == "percent") {
        	deviceType = "light"
        }
        
    //case "Volume Commands":
        if  (command == "mute" || command == "quiet" || command == "unmute" ) {
            deviceType = "volume" 
        }
        if  (command == "too loud") {
            command = "decrease"
            deviceType = "volume" 
        }
        if  (command == "not loud enough" || command == "too quiet") {
            command = "increase"
            deviceType = "volume"
        }
    //case "Fan Control Commands":
        if  (command == "slow down" || command == "too fast" ) {
            command = "decrease"
            deviceType = "fan" 
        }
        if  (command == "speed up" || command == "too slow") {
            command = "increase"
            deviceType = "fan" 
        }
		if (command == "high" || command == "medium"|| command == "low") {
			deviceType = "fan"                  
		}
    //case "Other Commands":           
        if (command == "lock" || command == "unlock") {
			deviceType = "lock"                  
		}
        if (command == "open" || command == "close") {
			deviceType = "door"                  
		}
    }
    return ["deviceType":deviceType, "command":command ]                          
}

/************************************************************************************************************
   Version/Copyright/Information/Help
************************************************************************************************************/
private def textAppName() {
	def text = app.label // Parent Name
}	
private def textVersion() {
	def text = "4.0"
}
/************************************************************************************************************
   Pre-set SOUNDS
************************************************************************************************************/
private loadText() {
		switch ( actionType) {
		case "Bell 1":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/bell1.mp3", duration: "10"]
			break;
		case "Bell 2":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/bell2.mp3", duration: "10"]
			break;
		case "Dogs Barking":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/dogs.mp3", duration: "10"]
			break;
		case "Fire Alarm":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/alarm.mp3", duration: "17"]
			break;
		case "Piano":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/piano2.mp3", duration: "10"]
			break;
		case "Lightsaber":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/lightsaber.mp3", duration: "10"]
			break;
		default:
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/bell1.mp3", duration: "10"]
			break;
        }
}
