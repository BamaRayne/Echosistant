/* 
 * EchoSistant - The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enabled Device.
 * 
 *		2/17/2017		Version:4.0 R.0.0.0		Public Release 
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
	name			: "EchoSistant",
    namespace		: "Echo",
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
                page name: "mDefaults" 
            	page name: "mSHMSec"
                	page name: "mSecuritySuite" // links Parent to Security Add-ON
    			page name: "mNotifyProfile" // links Parent to Notification Add-ON
            page name: "mProfiles" // links Parent to Profiles Add-ON 
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
                href "mIntent", title: "Main Home Control", description: mIntentD(), state: mIntentS(),
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"    
				href "mProfiles", title: "Configure Profiles", description: mRoomsD(), state: mRoomsS(),
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_msg.png"
				href "mSettings", title: "General Settings", description: mSettingsD(), state: mSettingsS(),
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"
				href "mSupport", title: "Install and Support", description: mSupportD(), state: mSupportS(),
					image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_About.png"                               
                    if (activateDashboard) {
                        href "mDashboard", title: "Dashboard", description: mDashboardD(), state: mDashboardS(),
                            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Dash.png"
                    }
                        href "mBonus", title: "The Current Mode is: ${location.currentMode}" + "\n"  +
                        "Smart Home Monitor Status is: ${location.currentState("alarmSystemStatus")?.value}", description: none
            }
		}
	}           
page name: "mIntent"
    def mIntent() {
    	dynamicPage (name: "mIntent", title: "", install: false, uninstall: false) {
			section("Devices used by EchoSistant") {
	            href "mDevices", title: "Select Devices", description: mDevicesD(), state: mDevicesS()
			}               
            section ("System and Device Control Defaults") {
                href "mDefaults", title: "Change Defaults", description: mDefaultsD(), state: mDefaultsS()
			}
            section ("Manage Home Security") {
            	href "mSecurity", title: "Home Security control options", description: mSecurityD(), state: mSecurityS()
            }   
		}
	}
    page name: "mDevices"    
        def mDevices(){
            dynamicPage(name: "mDevices", title: "",install: false, uninstall: false) {
                section ("Select devices", hideWhenEmpty: true){ }
                section ("Lights and Switches", hideWhenEmpty: true){  
                    input "cSwitch", "capability.switch", title: "Allow These Switch(es)...", multiple: true, required: false, submitOnChange: true                   
                    input "cFan", "capability.switchLevel", title: "Allow These Fan(s)...", multiple: true, required: false
                }     
                section ("Doors and Locks ", hideWhenEmpty: true){ 
                	input "cLock", "capability.lock", title: "Allow These Lock(s)...", multiple: true, required: false, submitOnChange: true
                    input "cDoor", "capability.garageDoorControl", title: "Allow These Garage Door(s)...", multiple: true, required: false, submitOnChange: true
                    input "cRelay", "capability.switch", title: "Allow These Garage Door Relay(s)...", multiple: false, required: false, submitOnChange: true
                    if (cRelay) input "cContactRelay", "capability.contactSensor", title: "Allow This Contact Sensor to Monitor the Garage Door Relay(s)...", multiple: false, required: false                
                }    
                section ("Climate Control", hideWhenEmpty: true){ 
                 	input "cTstat", "capability.thermostat", title: "Allow These Thermostat(s)...", multiple: true, required: false
                    input "cIndoor", "capability.temperatureMeasurement", title: "Allow These Device(s) to Report the Indoor Temperature...", multiple: true, required: false
                 	input "cOutDoor", "capability.temperatureMeasurement", title: "Allow These Device(s) to Report the Outdoor Temperature...", multiple: true, required: false
                    input "cVent", "capability.switchLevel", title: "Allow These Smart Vent(s)...", multiple: true, required: false
                } 
                section ("Sensors", hideWhenEmpty: true) {
                 	input "cMotion", "capability.motionSensor", title: "Allow These Motion Sensor(s)...", multiple: true, required: false
                    input "cContact", "capability.contactSensor", title: "Allow These Contact Sensor(s)...", multiple: true, required: false      
            		input "cWater", "capability.waterSensor", title: "Allow These Water Sensor(s)...", multiple: true, required: false                       
                    input "cPresence", "capability.presenceSensor", title: "Allow These Presence Sensors(s)...", multiple: true, required: false
                }
                section ("Media" , hideWhenEmpty: true){
                    input "cSpeaker", "capability.musicPlayer", title: "Allow These Media Player Type Device(s)...", required: false, multiple: true
                    input "cSynth", "capability.speechSynthesis", title: "Allow These Speech Synthesis Capable Device(s)", multiple: true, required: false
                    input "cMedia", "capability.mediaController", title: "Allow These Media Controller(s)", multiple: true, required: false
                     if (cMedia?.size() > 1) {
                     paragraph "NOTE: only the fist selected device is used by the Main intent. The additional devices MUST be used by Profiles"
                     }
                } 
                section ("Batteries", hideWhenEmpty: true ){
                    input "cBattery", "capability.battery", title: "Allow These Device(s) with Batteries...", required: false, multiple: true
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
                    section ("Fan Control") {            
                        input "cHigh", "number", title: "Alexa Adjusts High Level to 99% by default", defaultValue: 99, required: false
                        input "cMedium", "number", title: "Alexa Adjusts Medium Level to 66% by default", defaultValue: 66, required: false
                        input "cLow", "number", title: "Alexa Adjusts Low Level to 33% by default", defaultValue: 33, required: false
                        input "cFanLevel", "number", title: "Alexa Automatically Adjusts Ceiling Fans by using a scale of 1-100 (default is +/-33%)", defaultValue: 33, required: false
                    }
                    section ("Activity Defaults") {            
                        input "cLowBattery", "number", title: "Alexa Provides Low Battery Feedback when the Bettery Level falls below... (default is 25%)", defaultValue: 25, required: false
                        input "cInactiveDev", "number", title: "Alexa Provides Inactive Device Feedback when No Activity was detected for... (default is 24 hours) ", defaultValue: 24, required: false
                    }
					section ("Alexa Voice Settings") {            
                        input "pDisableContCmds", "bool", title: "Disable Conversation (Alexa no longer prompts for additional commands except for 'try again' if an error ocurs)?", required: false, defaultValue: false
                        input "pEnableMuteAlexa", "bool", title: "Disable Feedback (Silence Alexa - it no longer provides any responses)?", required: false, defaultValue: false
                        input "pUseShort", "bool", title: "Use Short Alexa Answers (Alexa provides quick answers)?", required: false, defaultValue: false
                    }
                    
                    section ("HVAC Filters Replacement Reminders", hideWhenEmpty: true, hideable: true, hidden: false) {            
						input "cFilterReplacement", "number", title: "Alexa Automatically Schedules HVAC Filter Replacement in this number of days (default is 90 days)", defaultValue: 90, required: false                        
                        input "cFilterSynthDevice", "capability.speechSynthesis", title: "Send Audio Notification when due, to this Speech Synthesis Type Device(s)", multiple: true, required: false
                        input "cFilterSonosDevice", "capability.musicPlayer", title: "Send Audio Notification when due, to this Sonos Type Device(s)", required: false, multiple: true   
                        if (cFilterSonosDevice) {
                            input "volume", "number", title: "Temporarily change volume", description: "0-100%", required: false
                            input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                        }
						if (location.contactBookEnabled){
                        	input "recipients", "contact", title: "Send Text Notification when due, to this recipient(s) ", multiple: true, required: false
           				}
                        else {      
                            input name: "sms", title: "Send Text Notification when due, to this phone(s) ", type: "phone", required: false
                        		paragraph "You may enter multiple phone numbers separated by semicolon (E.G. 8045551122;8046663344)"
                            input "push", "bool", title: "Send Push Notification too?", required: false, defaultValue: false
                        }
                     }                     
                }
        }
        page name: "mSecurity"    
            def mSecurity(){
                dynamicPage(name: "mSecurity", title: "",install: false, uninstall: false) {
                section ("Set PIN Number to Unlock Security Features") {
                    input "cPIN", "password", title: "Use this PIN for ALL Alexa Controlled Controls", default: false, required: false, submitOnChange: true
                    //input "cTempPIN", "password", title: "Guest PIN (expires in 24 hours)", default: false, required: false, submitOnChange: true
                }                                
                if (cPIN) {
                    section ("Configure Security Options for Alexa") {
                    	def routines = location.helloHome?.getPhrases()*.label.sort()
                        input "cMiscDev", "capability.switch", title: "Allow these Switches to be PIN Protected...", multiple: true, required: false, submitOnChange: true
                        input "cRoutines", "enum", title: "Allow these Routines to be PIN Protected...", options: routines, multiple: true, required: false
                        input "uPIN_SHM", "bool", title: "Enable PIN for Smart Home Monitor?", default: false
                        input "uPIN_Mode", "bool", title: "Enable PIN for Location Modes?", default: false
							if (cMiscDev) 			{input "uPIN_S", "bool", title: "Enable PIN for Switch(es)?", default: false}
                            if (cTstat) 			{input "uPIN_T", "bool", title: "Enable PIN for Thermostats?", default: false}
                            if (cDoor || cRelay) 	{input "uPIN_D", "bool", title: "Enable PIN for Doors?", default: false}
                            if (cLock) 				{input "uPIN_L", "bool", title: "Enable PIN for Locks?", default: false}
                    }
                }
                    section ("Access Security Suite") {
                        href "mSecuritySuite", title: "Tap to configure your Home Security Suite module", description: ""
                    } 
                        	
                section ("Smart Home Monitor Status Change Feedback", hideWhenEmpty: true, hideable: true, hidden: true){
                    input "fSecFeed", "bool", title: "Activate SHM status change announcements.", default: false, submitOnChange: true
                    if (fSecFeed) {    
                        input "shmSynthDevice", "capability.speechSynthesis", title: "On this Speech Synthesis Type Devices", multiple: true, required: false
                        input "shmSonosDevice", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true    
                        }
                        if (shmSonosDevice) {
                            input "volume", "number", title: "Temporarily change volume", description: "0-100%", required: false
                            input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                            }
                    }
                }
        	}
		page name: "mSecuritySuite"    
                    def mSecuritySuite() {
                        dynamicPage (name: "mSecuritySuite", title: "", install: true, uninstall: false) {
                            if (childApps.size()) {  
                                section("Security Suite",  uninstall: false){
                                    app(name: "security", appName: "SecuritySuite", namespace: "Echo", title: "Configure Security Suite", multiple: false,  uninstall: false)
                                }
                            }
                            else {
                                section("Security Suite",  uninstall: false){
                                    paragraph "NOTE : Looks like you haven't created any Profiles yet.\n \nPlease make sure you have installed the Rooms Smart App Add-on before creating a new Room!"
                                    app(name: "security", appName: "SecuritySuite", namespace: "Echo", title: "Configure Security Suite", multiple: false,  uninstall: false)
                                }
                            }
                       }
                    }
	page name: "mProfiles"    
        def mProfiles() {
            dynamicPage(name: "mProfiles", title:"", install: true, uninstall: false) {
				section ("Messaging and Control Profiles") {
                	href "mMainProfile", title: "View and Create Messaging and Control Profiles...", description: none
                    }
				if (notifyOn) {
        			section ("Manage Notifications") {
  						href "mNotifyProfile", title: "View and Create Notification Profiles...", description: none
            			//,image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"            			
				}
			}            
		}
	}
	page name: "mNotifyProfile"    
            def mNotifyProfile() {
                dynamicPage (name: "mNotifyProfile", title: "", install: true, uninstall: false) {
                    if (childApps.size()) {  
                        section("Notifications",  uninstall: false){
                            app(name: "notification", appName: "NotificationProfile", namespace: "Echo", title: "Create a new Notifications Profile", multiple: true,  uninstall: false)
                        }
                    }
                    else {
                        section("Notifications",  uninstall: false){
                            paragraph "NOTE: Looks like you haven't created any Notifications yet.\n \nPlease make sure you have installed the Rooms Smart App Add-on before creating a new Room!"
                            app(name: "notification", appName: "NotificationProfile", namespace: "Echo", title: "Create a new Notifications Profile", multiple: true,  uninstall: false)
                        }
                    }
             	}
        }
        page name: "mMainProfile"    
            def mMainProfile() {
                dynamicPage (name: "mMainProfile", title: "", install: true, uninstall: false) {
                    if (childApps.size()>0) {  
                        section("Message and Control Profiles",  uninstall: false){
                            app(name: "Profiles", appName: "Profiles", namespace: "Echo", title: "Create a New Message & Control Profile", multiple: true,  uninstall: false)
                        }
                    }
                    else {
                        section("Profiles",  uninstall: false){
                            paragraph "NOTE: Looks like you haven't created any Profiles yet.\n \nPlease make sure you have installed the Profiles Smart App Add-on before creating a new Profile!"
                            app(name: "Profiles", appName: "Profiles", namespace: "Echo", title: "Create a New Message & Control Profile", multiple: true,  uninstall: false)
						}
					}
				}
            }        
page name: "mSettings"  
	def mSettings(){
        dynamicPage(name: "mSettings", uninstall: true) {
                section("Debugging") {
                    input "debug", "bool", title: "Enable Debug Logging", default: true, submitOnChange: true 
                    }
                section ("Apache License"){
                    input "ShowLicense", "bool", title: "Show License", default: false, submitOnChange: true
                    def msg = textLicense()
                        if (ShowLicense) paragraph "${msg}"
                    }
                section ("Security Token", hideable: true, hidden: true) {
                	paragraph ("Log into the IDE on your computer and navigate to the Live Logs tab. Leave that window open, come back here, and open this section")
                    paragraph "The Security Tokens are now displayed in the Live Logs section of the IDE"
    				log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                    paragraph 	"Access token:\n"+
                                                "${state.accessToken}\n"+
                                                "Application ID:\n"+
                                                "${app.id}"
                    href "tokens", title: "Revoke/Reset Security Access Token", description: none
                }
                section("Tap below to remove the ${textAppName()} application.  This will remove ALL Profiles and the App from the SmartThings mobile App."){
                }	
			}             
		}
    page name: "mSkill"
        def mSkill(){
			dynamicPage(name: "mSkill", uninstall: false) {
                section ("List of Devices") {
                    href "mDeviceDetails", title: "View your List of Devices for copy & paste to the AWS Skill...", description: "", state: "complete" 
                }
                section ("List of Controls") {
                    href "mControls", title: "View your List of Controls for copy & paste to the AWS Skill...", description: "", state: "complete" 
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
         page name: "mControls"
            def mControls(){
                    dynamicPage(name: "mControls", uninstall: false) {
                    section ("LIST_OF_SYSTEM_CONTROLS") { 
                        def DeviceList = getControlDetails()
                            paragraph ("${DeviceList}")
                            log.info "\nLLIST_OF_SYSTEM_CONTROLS \n${DeviceList}"
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
page name: "mSupport"  
 def mSupport(){
        dynamicPage(name: "mSupport", uninstall: false) {
        	section ("EchoSistant Modules") {
            	paragraph "For the notifications and room feedback to be operational, they must be installed in the ST IDE and the toggles below must be activated"
                input "notifyOn", "bool", title: "Is the Notifications Module Installed? ", required: true, defaultValue: false
 				input "securityOn", "bool", title: "Is the Security Suite Module Installed?", required: true, defaultValue: false
                }
                section ("Amazon AWS Skill Details") {
					href "mSkill", title: "Tap to view setup data for the AWS Main Intent Skill...", description: ""
            		}
                section ("Directions, How-to's, and Troubleshooting") { 
 					href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant", title: "Tap to go to the EchoSistant Wiki", description: none,
                		image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/wiki.png"
                	}   
            	section ("AWS Lambda website") {
            		href url:"https://aws.amazon.com/lambda/", title: "Tap to go to the AWS Lambda Website", description: none,
                		image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_aws.png"
                	}
            	section ("Amazon Developer website") {    
   					href url:"https://developer.amazon.com/", title: "Tap to go to Amazon Developer website", description: none,
                		image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Skills.png"
					}
                section ("Developers", hideWhenEmpty: true){  
            		paragraph ("You can reach out to the Echosistant Developers with the following information: \n" + 
                	"Jason Headley \n"+
                	"Forum user name @bamarayne \n" +
                	"Bobby Dobrescu \n"+
                	"Forum user name @SBDobrescu")
                	}
                }	            	
            }   
page name: "mBonus"    
    def mBonus(){
        dynamicPage(name: "mBonus", title: "EchoSistant Bonus Features",install: false, uninstall: false) {
        section ("Home Status Dashboard") {
        	input "activateDashboard", "bool", title: "Activate the DashBoard on the Home Page", required: false, default: false, submitOnChange: true
        	}
        if (activateDashboard) {
		section ("Configure the DashBoard") {
        	href "mDashConfig", title: "Tap here to configure Dashboard", description: "", state: complete
			}
        }
	}
}        
page name: "mDashboard"
	def mDashboard(){
        dynamicPage(name: "mDashboard", uninstall: false) {
        if (mLocalWeather) {
            section("Today's Weather"){
                paragraph (mGetWeather())
                }
            def activeAlert = mGetWeatherAlerts()
            if (activeAlert){
                section("Active Weather Alerts"){
                    paragraph (mGetWeatherAlerts())
                }
            }
        }
        section ("ThermoStats and Temperature") {
        	def tStat1 = ThermoStat1
            def temp1 = (tStat1?.currentValue("temperature"))
            def setPC1 = (tStat1?.currentValue("coolingSetpoint"))
            def setPH1 = (tStat1?.currentValue("heatingSetpoint"))
            def mode1 = (tStat1?.currentValue("thermostatMode"))
            def oper1 = (tStat1?.currentValue("thermostatOperatingState"))
            def tStat2 = ThermoStat2
            def temp2 = (tStat2?.currentValue("temperature"))
            def setPC2 = (tStat2?.currentValue("coolingSetpoint"))
            def setPH2 = (tStat2?.currentValue("heatingSetpoint"))
            def mode2 = (tStat2?.currentValue("thermostatMode"))
            def oper2 = (tStat2?.currentValue("thermostatOperatingState"))
		if ("${mode1}" == "auto") 
        	paragraph "The ${tStat1} is ${temp1}°. The thermostat is in ${mode1} mode, the heat is set to ${setPH1}°, the cooling is set to ${setPC1}°, and it is currently ${oper1}."
        if ("${mode1}" == "cool")
            paragraph "The ${tStat1} is ${temp1}°. The thermostat is set to ${setPC1}°, is in ${mode1} mode and is currently ${oper1}."
        if ("${mode1}" == "heat")
            paragraph "The ${tStat1} is ${temp1}°. The thermostat is set to ${setPH1}°, is in ${mode1} mode and is currently ${oper1}."
        if ("${mode1}" == "off")
        	paragraph "The ${tStat1} thermostat is currently ${mode1}" 
		if ("${mode2}" == "auto") 
        	paragraph "The ${tStat2} is ${temp2}°. The thermostat is in ${mode2} mode, the heat is set to ${setPH2}°, the cooling is set to ${setPC2}°, and it is currently ${oper2}."
        if ("${mode2}" == "cool")
            paragraph "The ${tStat2} is ${temp2}°. The thermostat is set to ${setPC2}°, is in ${mode2} mode and is currently ${oper2}."
        if ("${mode2}" == "heat")
            paragraph "The ${tStat2} is ${temp2}°. The thermostat is set to ${setPH2}°, is in ${mode2} mode and is currently ${oper2}."
        if ("${mode2}" == "off")
        	paragraph "The ${tStat2} thermostat is currently ${mode2}" 
		}
		section ("Temperature Sensors") {
        	def Sens1temp = (tempSens1?.currentValue("temperature"))
            def Sens2temp = (tempSens2?.currentValue("temperature"))
            def Sens3temp = (tempSens3?.currentValue("temperature"))
            def Sens4temp = (tempSens4?.currentValue("temperature"))
            def Sens5temp = (tempSens5?.currentValue("temperature"))
            if (tempSens1)
            	paragraph "The temperature of the ${tempSens1} is ${Sens1temp}°."
            if (tempSens2)
            	paragraph "The temperature of the ${tempSens2} is ${Sens2temp}°."
            if (tempSens3)
            	paragraph "The temperature of the ${tempSens3} is ${Sens3temp}°."
            if (tempSens4)
            	paragraph "The temperature of the ${tempSens4} is ${Sens4temp}°."
            if (tempSens5)
            	paragraph "The temperature of the ${tempSens5} is ${Sens5temp}°."
			}
		} 
	} 
page name: "mDashConfig"
	def mDashConfig(){
        dynamicPage(name: "mDashConfig", uninstall: false) {
        section ("Local Weather") {
        	input "mLocalWeather", "bool", title: "Display local weather conditions on Dashboard", required: false, default: false, submitOnChange: true
            }
        if (mLocalWeather) {
		section ("Local Weather Information") {
            href "mWeatherConfig", title: "Tap here to configure Weather information on Dashboard", description: "", state: complete
			}
        }            
		section ("Thermoststats") {
        	input "ThermoStat1", "capability.thermostat", title: "First ThermoStat", required: false, default: false, submitOnChange: true 
        	input "ThermoStat2", "capability.thermostat", title: "Second ThermoStat", required: false, default: false, submitOnChange: true 
            }
        section ("Temperature Sensors") {
        	input "tempSens1", "capability.temperatureMeasurement", title: "First Temperature Sensor", required: false, default: false, submitOnChange: true 
            input "tempSens2", "capability.temperatureMeasurement", title: "Second Temperature Sensor", required: false, default: false, submitOnChange: true 
            input "tempSens3", "capability.temperatureMeasurement", title: "Third Temperature Sensor", required: false, default: false, submitOnChange: true 
            input "tempSens4", "capability.temperatureMeasurement", title: "Fourth Temperature Sensor", required: false, default: false, submitOnChange: true 
            input "tempSens5", "capability.temperatureMeasurement", title: "Fifth Temperature Sensor", required: false, default: false, submitOnChange: true 
        }
    }
}
def mWeatherConfig() {
	dynamicPage(name: "mWeatherConfig", title: "Weather Settings") {
		section {
    		input "wImperial", "bool", title: "Report Weather In Imperial Units\n(°F / MPH)", defaultValue: "true", required: "false"
            input "wZipCode", "text", title: "Zip Code (If Location Not Set)", required: "false"
            paragraph("Currently forecast is automatically pulled from getWeatherFeature your location must be set in your SmartThings app for this to work.")
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
    path("/s") { action: [GET: "controlSecurity"] }
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
        def children = getChildApps()
    	if (debug) log.debug "Refreshing Profiles for CoRE, ${getChildApps()*.label}"
		if (!state.accessToken) {
        	if (debug) log.error "Access token not defined. Attempting to refresh. Ensure OAuth is enabled in the SmartThings IDE."
                OAuthToken()
			}
        //SHM status change and keypad initialize
    		subscribe(location, locationHandler)
    		subscribe(location, "alarmSystemStatus",alarmStatusHandler)//used for ES speaker feedback
			def event = [name:"alarmSystemStatus", value: location.currentState("alarmSystemStatus").value, 
						displayed: true, description: "System Status is ${shmState}"]
        //State Variables            
            state.lastMessage = null
            state.lastIntent  = null
            state.lastTime  = null
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
            state.scheduledHandler
            state.filterNotif = null
            state.lastAction = null
			state.lastActivity = null
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
    def versionTxt  = params.versionTxt 		
    def versionDate = params.versionDate
    def releaseTxt = params.releaseTxt
    def event = params.intentResp
        state.lambdaReleaseTxt = releaseTxt
        state.lambdaReleaseDt = versionDate
        state.lambdatextVersion = versionTxt
    def versionSTtxt = textVersion() 
    def pPendingAns = false 
    def pContinue = state.pMuteAlexa
    def pShort = state.pShort
    def String outputTxt = (String) null 
    	state.pTryAgain = false
	
    if (debug) log.debug "^^^^____LAUNCH REQUEST___^^^^" 
    if (debug) log.debug "Launch Data: (event) = '${event}', (ver) = '${versionTxt}', (date) = '${versionDate}', (release) = '${releaseTxt}'"
try {
    if (event == "noAction") {//event == "AMAZON.NoIntent" removed 1/20/17
    	state.pinTry = null
        state.savedPINdata = null
        state.pContCmdsR = null // added 1/20/2017
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
		childApps.each {child ->
			if (child.label.toLowerCase() == event.toLowerCase()) { 
                pContinue = child?.checkState()   
            }
       	}
        //if Alexa is muted from the child, then mute the parent too / MOVED HERE ON 2/9/17
        pContinue = pContinue == true ? true : state.pMuteAlexa == true ? true : pContinue
		return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]	     
	}
	if (debug){
    	log.debug "Begining Process data: (event) = '${event}', (ver) = '${versionTxt}', (date) = '${versionDate}', (release) = '${releaseTxt}'"+ 
      	"; data sent: pContinue = '${pContinue}', pShort = '${pShort}',  pPendingAns = '${pPendingAns}', versionSTtxt = '${versionSTtxt}', outputTxt = '${outputTxt}' ; "+
        "other data: pContCmdsR = '${state.pContCmdsR}', pinTry'=${state.pinTry}' "
	}
    return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]	 

} catch (Throwable t) {
        log.error t
        outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return ["outputTxt":outputTxt, "pContinue":pContinue, "pShort":pShort, "pPendingAns":pPendingAns, "versionSTtxt":versionSTtxt]
	}
}   
/************************************************************************************************************
		FEEDBACK - from Lambda via page f
************************************************************************************************************/
def feedbackHandler() {
    //LAMBDA
    def fDevice = params.fDevice
   	def fQuery = params.fQuery
    def fOperand = params.fOperand 
    def fCommand = params.fCommand 
    def fIntentName = params.intentName
    def pPIN = false
    //OTHER 
    def String deviceType = (String) null
    def String outputTxt = (String) null
    def String deviceM = (String) null
	def currState
    def stateDate
    def stateTime
	def data = [:]
    	fDevice = fDevice.replaceAll("[^a-zA-Z0-9 ]", "") 
    if (debug){
    	log.debug 	"Feedback data: (fDevice) = '${fDevice}', "+
    				"(fQuery) = '${fQuery}', (fOperand) = '${fOperand}', (fCommand) = '${fCommand}', (fIntentName) = '${fIntentName}'"}
	def fProcess = true
    state.pTryAgain = false

try {
		
        fOperand = fOperand == "lights on" ? "lights" : fOperand == "switches on" ? "lights" : fOperand == "switches" ? "lights" : fOperand
        fCommand = fOperand == "lights on" ? "on" : fOperand == "switches on" ? "on" : fCommand
    
    if (fDevice == "undefined" && fQuery == "undefined" && fOperand == "undefined" && fCommand == "undefined") {
		outputTxt = "Sorry, I didn't get that, "
        state.pTryAgain = true
        state.pContCmdsR = "clear"
        state.lastAction = null
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
	}    
    else {
    	if (fDevice != "undefined" && fQuery != "undefined" && fOperand == "undefined" && fQuery != "about" && fQuery != "get" ) {
            def dMatch = deviceMatchHandler(fDevice)
            if (dMatch?.deviceMatch == null) { 				
                outputTxt = "Sorry, I couldn't find any details about " + fDevice
                state.pTryAgain = true
        		return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }
            else {
                def dDevice = dMatch?.deviceMatch
                def dType = dMatch?.deviceType
                def dState = dMatch?.currState
                def dMainCap = dMatch?.mainCap
                def dCapCount = getCaps(dDevice,dType, dMainCap, dState)
                state.pContCmdsR = "caps"
                	
                    if (state.pShort != true){ 
                    outputTxt = "I couldn't quite get that, but " + fDevice +  " has " + dCapCount + " capabilities. Would you like to hear more about this device?"         
                	}
                    else {outputTxt = "I didn't catch that, but " + fDevice +  " has " + dCapCount + " capabilities. Want to hear more?"} 
        			return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }
        }
        if (fOperand == "undefined" && fQuery != "undefined" && fQuery != "who" && !fQuery.contains ("when")) {        
                def deviceMatch=cTstat.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
                    if(deviceMatch)	{
                            deviceType = "cTstat"
                            def currentMode = deviceMatch.latestValue("thermostatMode")
                            def currentHSP = deviceMatch.latestValue("heatingSetpoint") 
                            def currentCSP = deviceMatch.latestValue("coolingSetpoint") 
                            def currentTMP = deviceMatch.latestValue("temperature")
                            int temp = currentTMP
                            int hSP = currentHSP
                            int cSP = currentCSP
                            stateDate = deviceMatch.currentState("temperature").date
                            stateTime = deviceMatch.currentState("temperature").date.time
                            def timeText = getTimeVariable(stateTime, deviceType)            
                            outputTxt = "The " + fDevice + " temperature is " + temp + " degrees and the current mode is " + currentMode + " , with set points of " + cSP + " for cooling and " + hSP + " for heating"
        					return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                    else {
                        if (fDevice != "undefined") {
                             if (fDevice != "undefined"){
                                def rSearch = deviceMatchHandler(fDevice)
                                    if (rSearch?.deviceMatch == null) { 
                                        outputTxt = "Sorry, I couldn't find any details about " + fDevice
                                        state.pTryAgain = true
        								return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                    }
                                    else {
                                        deviceM = rSearch?.deviceMatch
                                        outputTxt = deviceM + " has been " + rSearch?.currState + " since " + rSearch?.tText
                                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
                                    }                  
                                if (rSearch.deviceType == "cBattery") {
                                    outputTxt = "The battery level for " + deviceM + " is " + rSearch.currState + " and was last recorded " + rSearch.tText
                                	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                                if (rSearch.deviceType == "cMedia") {
                                    outputTxt = rSearch.currState + " since " + rSearch.tText
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                            }
                        }
                        else {
                            outputTxt = "Sorry, I didn't get that, "
                            state.pTryAgain = true
                            state.pContCmdsR = "clear"
                            state.lastAction = null
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
					}  
        }
        else {
//>>> Temp >>>>      
            if(fOperand == "temperature") {
                if(cTstat){
                    cTstat.find {s -> 
                        if(s.label.toLowerCase() == fDevice.toLowerCase()){
                            deviceType = "cTstat"
                            def currentTMP = s.latestValue("temperature")
                            int temp = currentTMP
                            stateDate = s.currentState("temperature").date
                            stateTime = s.currentState("temperature").date.time
                            def timeText = getTimeVariable(stateTime, deviceType)            
                            outputTxt = "The temperature " + fDevice + " is " + temp + " degrees and was recorded " + timeText.currDate + " at " + timeText.currTime
                        }
                    }
                    if (outputTxt != null) {
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }            
                }
                if(cMotion){
                    cMotion.find {s -> 
                        if(s.label.toLowerCase() == fDevice.toLowerCase()){
                            deviceType = "cTstat"
                            def currentTMP = s.latestValue("temperature")
                            int temp = currentTMP
                            stateDate = s.currentState("temperature").date
                            stateTime = s.currentState("temperature").date.time
                            def timeText = getTimeVariable(stateTime, deviceType)
                            outputTxt = "The temperature in the " + fDevice + " is " + temp + " degrees and was recorded " + timeText.currDate + " at " + timeText.currTime
                        }
                    }
                    if (outputTxt != null) {
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }            
                }
                if(cWater){
                    cWater.find {s -> 
                        if(s.label.toLowerCase() == fDevice.toLowerCase()){
                            deviceType = "cWater"
                            def currentTMP = s.latestValue("temperature")
                            int temp = currentTMP
                            stateDate = s.currentState("temperature").date
                            stateTime = s.currentState("temperature").date.time
                            def timeText = getTimeVariable(stateTime, deviceType)            
                            outputTxt = "The temperature of " + fDevice + " is " + temp + " degrees and was recorded " + timeText.currDate + " at " + timeText.currTime
                        }
                    }
                    if (outputTxt != null) {
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                }            
                if (outputTxt == null && fDevice != "undefined") { 
                    outputTxt = "Device named " + fDevice + " doesn't have a temperature sensor" 
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }
                else {
                    if(cIndoor){
                        def sensors = cIndoor.size()
                        def tempAVG = cIndoor ? getAverage(cIndoor, "temperature") : "undefined device"          
                        def currentTemp = tempAVG
                        outputTxt = "The indoor temperature is " + currentTemp
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                    else {
                    	if(state.pShort != true) {
                     		outputTxt = "Sorry, I couldn't quite get that, what device would you like to use to get the indoor temperature?"
                        }
                        else {outputTxt = "Oops, I didn't get that, what device?"}
                		return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                } 
            }
//>>> Temp >>>>>
            if (fOperand == "temperature inside" || fOperand == "indoor temperature" || fOperand == "temperature is inside"){
                if(cIndoor){
                    def sensors = cIndoor.size()
                    def tempAVG = cIndoor ? getAverage(cIndoor, "temperature") : "undefined device"          
                    def currentTemp = tempAVG
                    outputTxt = "The indoor temperature is " + currentTemp
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }
                else {
                    outputTxt = "There are no indoor sensors selected, please go to the SmartThings app and select one or more sensors"
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }                            
            }
//>>> Temp >>>>
            if (fOperand == "temperature outside" || fOperand == "outdoor temperature" || fOperand == "temperature is outside"){
                if(cOutDoor){
                    def sensors = cOutDoor.size()
                    def tempAVG = cOutDoor ? getAverage(cOutDoor, "temperature") : "undefined device"          
                    def currentTemp = tempAVG
                    def forecastT = mGetWeatherTemps()
                    outputTxt = forecastT + ",. The current temperature is " + currentTemp
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }
                else {
                    outputTxt = "There are no outdoor sensors selected, go to the SmartThings app and select one or more sensors"
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]		
                }                            
            }
//>>> Weather >>>>
			if (fOperand == "weather" || fOperand == "weather conditions" || fOperand == "current weather"){
                    outputTxt = mGetWeather()
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]				
            }
            if (fOperand == "weather alert" || fOperand == "alerts" || fOperand == "weather alerts"){
                    outputTxt = mGetWeatherAlerts()
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]		
            }            
//>>> Mode >>>>
            if (fOperand == "mode" ){
                    outputTxt = "The Current Mode is " + location.currentMode      
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]			
            }
//>>> Security >>>>
            //TO DO: restrict security based on command
            if (fOperand == "security" || fOperand == "smart home monitor" || fOperand == "alarm" ){
                    def sSHM = location.currentState("alarmSystemStatus")?.value       
                    sSHM = sSHM == "off" ? "disabled" : sSHM == "away" ? "Armed Away" : sSHM == "stay" ? "Armed Stay" : "unknown"
                    outputTxt = "Your Smart Home Monitor Status is " +  sSHM
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]				
            }
//>>> Lights >>>>            
            if (fOperand == "lights" && fCommand != "undefined") { 
                if(cSwitch){
                    def devList = []
                    if (cSwitch.latestValue("switch").contains(fCommand)) {
                        cSwitch.each { deviceName ->
                                    if (deviceName.latestValue("switch")=="${fCommand}") {
                                        String device  = (String) deviceName
                                        devList += device
                                    }
                        }
                    }
                    if (fQuery == "how" || fQuery.contains ("if") || fQuery == "are there") { // removed fQuery == "undefined" 2/13/2017
                        if (devList.size() > 0) {
                            if (devList.size() == 1) {
                                outputTxt = "There is one switch " + fCommand + " , would you like to know which one"                           			
                            }
                            else {
                                outputTxt = "There are " + devList.size() + " switches " + fCommand + " , would you like to know which switches"
                            }
                        data.devices = devList
                        data.cmd = fCommand
                        data.deviceType = "cSwitch"
                        state.lastAction = data
                        state.pContCmdsR = "feedback"
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	

                        }
                        else {outputTxt = "There are no switches " + fCommand}
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	

                    }
                    else if (fQuery.contains ("what") || fQuery.contains ("which") || fQuery == "what's") {
                        def devNames = []
                        if (cSwitch.latestValue("switch").contains(fCommand)) {
                            cSwitch.each { deviceName ->
                                        if (deviceName.latestValue("switch")=="${fCommand}") {
                                            String device  = (String) deviceName
                                            devNames += device
                                        }
                            }
                        outputTxt = "The following switches are " + fCommand + "," + devNames.sort().unique()
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	

                        }
                        else {outputTxt = "There are no switches " + fCommand}
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
                    }     
                }
            }
//>>> Doors >>>>            
            if(fOperand.contains("doors")) { // && fCommand != "undefined") { removed 1/23/2017
                    def devList = []
                    if (cContact.latestValue("contact").contains(fCommand) || cDoor.latestValue("door").contains(fCommand)) {
                        cContact.each { deviceName ->
                                    if (deviceName.latestValue("contact")=="${fCommand}") {
                                        String device  = (String) deviceName
                                        devList += device
                                    }
                        }
                        cDoor?.each { deviceName ->
                                    if (deviceName.latestValue("door")=="${fCommand}") {
                                        String device  = (String) deviceName
                                        devNames += device
                                    }
                        }                    
                    }
                    if (fQuery == "how" || fQuery== "how many" || fQuery == "arere") { // removed fQuery == "undefined" 2/13
                        if (devList.size() > 0) {
                            if (devList.size() == 1) {
                                outputTxt = "There is one door or window " + fCommand + " , would you like to know which one"                           			
                            }
                            else {
                                outputTxt = "There are " + devList.size() + " doors or windows " + fCommand + " , would you like to know which doors or windows"
                            }
                        data.devices = devList
                        data.cmd = fCommand
                        data.deviceType = "cContact"
                        state.lastAction = data
                        state.pContCmdsR = "feedback"
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	

                        }
                        else {outputTxt = "There are no doors or windows " + fCommand}
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
                    }
                    else if (fQuery.contains ("what") || fQuery.contains ("which") || fQuery == "what's") {
                        def devNames = []
                        fOperand = fOperand.contains("closed") ? "closed" : fOperand.contains("open") ? "open" : fOperand 
                        fCommand = fCommand.contains("close") ? "closed" : fCommand
                        fCommand = fOperand == "closed" ? "closed" : fOperand == "open" ? "open" : fCommand                  
                            if (cContact.latestValue("contact").contains(fCommand) || cDoor.latestValue("door").contains(fCommand)) {
                                cContact?.each { deviceName ->
                                            if (deviceName.latestValue("contact")=="${fCommand}") {
                                                String device  = (String) deviceName
                                                devNames += device
                                            }
                                }
                                cDoor?.each { deviceName ->
                                            if (deviceName.latestValue("door")=="${fCommand}") {
                                                String device  = (String) deviceName
                                                devNames += device
                                            }
                                }
                            outputTxt = "The following doors or windows are " + fCommand + "," + devNames.sort().unique()
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	

                            }
                            else {outputTxt = "There are no doors or windows " + fCommand}
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
                    }
            }
//>>> Battery Level >>>>                        
            if(fOperand == "batteries" || fOperand == "battery level" || fOperand == "battery" ) {
            	def cap = "bat"
            	def dMatch = deviceMatchHandler(fDevice)	
                if (dMatch?.deviceMatch == null) { 		
                def devList = getCapabilities(cap)
                if (fQuery == "how" || fQuery== "how many" || fQuery == "undefined" || fQuery == "are there" || fCommand == "low" || fQuery == "give" || fQuery == "get") {
                        if (devList.listSize > 0) {
                            if (devList.listSize == 1) {
                                outputTxt = "There is one device with low battery level , would you like to know which one"                           			
                            }
                            else {
                                outputTxt = "There are " + devList.listSize + " devices with low battery levels, would you like to know which devices"
                            }
                        def sdevices = devList?.listBat
                        def devListString = sdevices.join(",")
                        data.list = devListString
                        state.lastAction = devListString
                        state.pContCmdsR = "bat"
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
                        }
                        else {outputTxt = "There are no devices with low battery levels"}
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
                    }
                    else if (fQuery.contains ("what") || fQuery.contains ("which")) {
                        if (devList.listSize > 0) {
                        outputTxt = "The following devices have low battery levels " + devList.listBat.sort()//.unique()
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
                        }
                        else {outputTxt = "There are no devices with low battery levels "
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
                        } 
                    }
                 }
                 else {
					device = dMatch.deviceMatch
                    currState = device.currentState("battery").value
					stateTime = device.currentState("battery").date.time
                	def timeText = getTimeVariable(stateTime, deviceType)
					outputTxt = "The battery level of " + fDevice + " is " + currState + " percent and was registered " + timeText.currDate + " at " + timeText.currTime
                  	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]                    
 				}                   
            }
//>>> Inactive Devices >>>>                               
            if(fOperand == "inactive" || fOperand.contains("inactive") ||  fCommand == "inactive" ) { //devices inactive
            	def cap = "act"
            	def devList = getCapabilities(cap)
                if (fQuery == "how" || fQuery== "how many" || fQuery == "undefined" || fQuery == "are there" || fQuery == "give" || fQuery == "get") {
                        if (devList.listSize > 0) {
                            if (devList.listSize == 1) {
                                outputTxt = "There is one inactive device, would you like to know which one?"                           			
                            }
                            else {
                                outputTxt = "There are " + devList.listSize + " inactive devices, would you like to know which devices"
                            }
                        def sdevices = devList?.listDev
                        def devListString = sdevices.join(",")
                        data.list = devListString
                        state.lastAction = devListString
                        state.pContCmdsR = "act"
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
                        else {outputTxt = "There are no inactive devices"}
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                    else if (fQuery.contains ("what") || fQuery.contains ("which")) {
                        if (devList.listSize > 0) {
                        outputTxt = "The following devices have been inactive for more than " + cInactiveDev + " hours " + devList.listDev.sort()
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
                        else {outputTxt = "There are no inactive devices"
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        } 
                    }
            }       
//>>> Settings >>>>                                    
            if(fOperand == "settings") {
                def pCmds = state.pContCmds == true ? "enabled" : "disabled"
                def pCmdsR = state.pContCmdsR //last continuation response
                def pMute = state.pMuteAlexa == true ? "Alexa voice is disabled" : "Alexa voice is active"
                //state.scheduledHandler
                def pin_D = state.usePIN_D 			== true ? "active" : "inactive"
                def pin_L = state.usePIN_L 			== true ? "active" : "inactive"
                def pin_T = state.usePIN_T 			== true ? "active" : "inactive"
                def pin_S = state.usePIN_S 			== true ? "active" : "inactive"
                def pin_SHM = state.usePIN_SHM 		== true ? "active" : "inactive"
                def pin_Mode = state.usePIN_Mode 	== true ? "active" : "inactive" 

                def activePin 	= pin_D 	== "active" ? "doors" : null
                    activePin  	= pin_L 	== "active" ? activePin + ", locks" : activePin
                    activePin  	= pin_S 	== "active" ? activePin + ", switches" : activePin
                    activePin  	= pin_T 	== "active" ? activePin + ", thermostats"  : activePin
                    activePin  	= pin_SHM 	== "active" ? activePin + ", smart security"  : activePin
                    activePin  	= pin_Mode 	== "active" ? activePin + ", modes"  : activePin
                if (activePin == null) { activePin = "no groups"}                
                def inactivePin = pin_D 	== "inactive" ? "doors" : null
                    inactivePin  = pin_L 	== "inactive" ? inactivePin + ", locks" : inactivePin
                    inactivePin  = pin_S 	== "inactive" ? inactivePin + ", switches" : inactivePin
                    inactivePin  = pin_T 	== "inactive" ? inactivePin + ", thermostats" : inactivePin
                    inactivePin  = pin_SHM	== "inactive" ? inactivePin + ", smart security" : inactivePin
                    inactivePin  = pin_Mode	== "inactive" ? inactivePin + ", location modes" : inactivePin
                if (inactivePin == null) {inactivePin = "no groups"}
  
                outputTxt = pMute + " and the conversational module is " + pCmds + ". The pin number is active for: " +  activePin + " and inactive for: " + inactivePin
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }
//>>> Presence >>>>                                    
            if (fQuery == "who" ) {
                if(cPresence){
                        def devListP = []
                        def devListNP = []
                        if (cPresence.latestValue("presence").contains("present")) {
                            cPresence.each { deviceName ->
                                        if (deviceName.latestValue("presence")=="present") {
                                            String device  = (String) deviceName
                                            devListP += device
                                        }
                            }
                        }
                        if (cPresence.latestValue("presence").contains("not present")) {
                            cPresence.each { deviceName ->
                                        if (deviceName.latestValue("presence")=="not present") {
                                            String device  = (String) deviceName
                                            devListNP += device
                                        }
                            }
                        }
                    if (fOperand == "here" || fOperand == "at home" || fOperand == "present" || fOperand == "home" ) {
                            if (devListP.size() > 0) {
                                if (devListP.size() == 1) {
                                    outputTxt = "Only" + devListP + "is at home"                         			
                                }
                                else {
                                    outputTxt = "The following " + devListP.size() + " people are at home: " + devListP
                                }

                            }
                            else outputTxt = "No one is home"
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                    else if (fOperand.contains("not")) {
                        if (devListNP.size() > 0) {
                            if (devListNP.size() == 1) {
                                    outputTxt = "Only" + devListNP + "is not home"                         			
                            }
                            else {
                                    outputTxt = "The following " + devListNP.size() + " people are not at home: " + devListNP
                            }
                        }
                        else outputTxt = "Everyone is at home"
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                }
            }
//>>> Events >>>>                                    
            if (fQuery.contains ("when")) {
            	fCommand = fCommand == "changed" ? "change" : fCommand
            	if (fCommand == "change" && state.filterNotif !=null ) {
                	outputTxt = state.filterNotif
                	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }
                else {
                	def deviceData = deviceMatchHandler(fDevice)
                	deviceM  = deviceData.deviceMatch  
                	outputTxt = deviceM + " was last " + fOperand + " " + deviceData.tText
                	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            	}
            }      
            def hText = fDevice != "undefined" ? " a device named " + fDevice : " something "           
                if (state.pShort != true){ 
					outputTxt = "Sorry, I heard that you were looking for feedback on  " + hText + " but Echosistant wasn't able to help, "        
                }
                else {outputTxt = "I've heard " + hText +  " but I wasn't able to provide any feedback "} 
            state.pTryAgain = true
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        }
    } 

}catch (Throwable t) {
        log.error t
        outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
}

}
/************************************************************************************************************
   DEVICE CONTROL - from Lambda via page c
************************************************************************************************************/
def controlDevices() {
		//FROM LAMBDA
        def ctCommand = params.cCommand
        def ctNum = params.cNum
        def ctPIN = params.cPIN
        def ctDevice = params.cDevice
        def ctUnit = params.cUnit
        def ctGroup = params.cGroup       
		def ctIntentName = params.intentName
        //OTHER VARIABLES
        def String outputTxt = (String) null 
		def pPIN = false
        def String deviceType = (String) null
        def String command = (String) null
		def String numText = (String) null
        def String result = (String) null
        def String activityId = (String) "undefined"
        def delay = false
        def data
        ctDevice = ctDevice.replaceAll("[^a-zA-Z0-9 ]", "")

        if (debug) log.debug "Control Data: (ctCommand)= ${ctCommand}',(ctNum) = '${ctNum}', (ctPIN) = '${ctPIN}', "+
                             "(ctDevice) = '${ctDevice}', (ctUnit) = '${ctUnit}', (ctGroup) = '${ctGroup}', (ctIntentName) = '${ctIntentName}'"
	def ctProcess = true	
    state.pTryAgain = false 
try {	   
    if (ctIntentName == "main") {
        ctPIN = ctPIN == "?" ? "undefined" : ctPIN
        if (ctNum == "undefined" || ctNum =="?") {ctNum = 0 } 
        if (ctCommand =="?") {ctCommand = "undefined"} 
        ctNum = ctNum as int
    	if (ctCommand == "undefined" || ctNum == "undefined" || ctPIN == "undefined" || ctDevice == "undefined" || ctUnit == "undefined" || ctGroup == "undefined") {        
            if (ctUnit =="?" || ctUnit == "undefined") {
                def String unit =  (String) "undefined"
            }    
            else {
                if (ctNum>0){
                    def getTxt = getUnitText(ctUnit, ctNum)     
                    numText = getTxt.text
                    ctUnit = getTxt.unit
                }
            }   
            if (ctNum > 0 && ctDevice != "undefined" && ctCommand == "undefined") {
                ctCommand = "set"
            }
            if (state.pinTry != null) {
                if (ctCommand == "undefined" && ctDevice == "undefined") {
                    outputTxt = pinHandler(ctPIN, ctCommand, ctNum, ctUnit)
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }
                else {
                state.pinTry = null
                state.pTryAgain = false  
                }
            }
            if (ctCommand != "undefined") {
                if (ctCommand.contains ("try again") && state.lastAction != null ) {
                        def savedData = state.lastAction
                        outputTxt = controlHandler(savedData)
                }       
                else {
                    outputTxt = getCustomCmd(ctCommand, ctUnit, ctGroup, ctNum) //added ctNum 1/27/2017
                    if (ctCommand.contains ("try again")) {
                        state.pContCmdsR = "clear"
                        state.savedPINdata = null
                        state.pinTry = null
                        outputTxt = " I am sorry for the trouble. I am getting my act together now, so you can continue enjoing your Echosistant app"
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
                }
                if (outputTxt!= null ) {
                        if (ctUnit == "pin number" || ctUnit == "pin") {
                            if (ctGroup == "thermostats" || ctGroup == "locks" || ctGroup == "doors" || ctGroup == "security" || ctGroup == "switches") {
                                state.pTryAgain = false
                            }
                            else {
                                state.pTryAgain = true
                            }
                        }
                        if (outputTxt == "Pin number please") {pPIN = true}
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }
                else {
                    def  getCMD = getCommand(ctCommand, ctUnit) 
                    deviceType = getCMD.deviceType
                    command = getCMD.command
                }
            }
    		//>>> MAIN PROCESS STARTS <<<<        
            if (deviceType == "volume" || deviceType == "general" || deviceType == "light") {      
                        def deviceMatch = null
                        //def activityId = null 2/11/2017 moved as global variable
                        def dType = null
                            if (settings.cSpeaker?.size()>0) {
                                deviceMatch = cSpeaker.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}
                                if(deviceMatch) {
                                log.warn "found a speaker "
                                dType = "v"}
                            }
                            if (deviceMatch == null && settings.cSynth?.size()>0) {
                                deviceMatch = cSynth.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}                 
                                if(deviceMatch) {dType = "v"}
                            }
							//HARMONY PROCESS//
							if (deviceMatch == null && settings.cMedia?.size()>0) {
                                //deviceMatch = cMedia.first()
                                deviceMatch = cMedia.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}                 
                                if(deviceMatch) {
                                    dType = "m"
                                }
                                else {
                                    //cMedia.each {a -> //disabled 2/13/2017 to ONLY use Main Hub        
                                        //def activities = a.currentState("activities").value //disabled 2/13/2017 to ONLY use Main Hub
                                        def harmonyMain = cMedia.first()
                                        def activities = harmonyMain.currentState("activities").value
                                        def activityList = new groovy.json.JsonSlurper().parseText(activities)
                                            activityList.each { it ->  
                                                def activity = it
                                                    if(activity.name.toLowerCase() == ctDevice.toLowerCase()) {
                                                    dType = "m"
                                                    deviceMatch = harmonyMain //a //disabled 2/13/2017 to ONLY use Main Hub
                                                    activityId = activity.id
                                                    }    	
                                            }
                                  	//}   //disabled 2/13/2017 to ONLY use Main Hub
                                }
                            }
                            //Personal Preference to use the Harmony Hub for TV off (works only with first Hub selected 2/10/17 Bobby
                            if (ctDevice == "TV" && command != "mute" && command != "unmute" && command != "setLevel" && command != "decrease" && command != "increase" && settings.cMedia?.size()>0) {
                            	dType = "m"
                                deviceMatch = cMedia.first()                   
                            }    
                            if (deviceMatch == null && settings.cSwitch?.size()>0 && state.pinTry == null) {
                                deviceMatch = cSwitch.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}                 
                                if(deviceMatch) {dType = "s"}
                            }
                            if (deviceMatch == null && settings.cMiscDev?.size()>0 && state.pinTry == null) {
                                deviceMatch = cMiscDev.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}                 
                                if(deviceMatch) { 
                            //>>>>>>>  CHECK FOR ENABLED PIN <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                                if(cPIN && state.usePIN_S == true && deviceMatch) {
                                    if (debug) log.warn "PIN enabled for Switch '${deviceMatch}'"
                                    device = deviceMatch.label
                                    if (command == "disable" || command == "deactivate"|| command == "stop") {command = "off"}
                                    if (command == "enable" || command == "activate"|| command == "start") {command = "on"}	                        
                                    ctUnit = ctUnit == "minute" ? "minutes" : ctUnit
                                    delay = true
                                    data = [type: "cMiscDev", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                    state.lastAction = data
                                    state.pContCmdsR = "cMiscDev"
                            //>>>>>>>  RUN PIN VALIDATION PROCESS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                                    def pin = "undefined"
                                    command = "validation"
                                    def unit = "cMiscDev"
                                    outputTxt = pinHandler(pin, command, ctNum, unit)
                                    pPIN = true
                                    if (state.pinTry == 3) {pPIN = false}
                                    log.warn "try# ='${state.pinTry}'"
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                                else {               
                                    if (ctNum > 0 && ctUnit == "minutes") {
                                        runIn(ctNum*60, controlHandler, [data: data])
                                        if (command == "on" || command == "off" ) {outputTxt = "Ok, turning " + ctDevice + " " + command + ", in " + numText}
                                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                    }
                                    else {
                                        delay = true
                                        data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                        outputTxt = controlHandler(data)
                                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                    }
                                } 
                            }
                        }
                        if (deviceMatch && dType == "v") {
                            device = deviceMatch
                            delay = false
                            data = [type: "cVolume", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                            outputTxt = controlHandler(data)
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
    					//HARMONY CONTROL//                  
                        if (deviceMatch && dType == "m") {
                            device = deviceMatch
                            if (ctNum > 0 && ctUnit == "minutes") {
                                log.warn "delay Harmony activity = ${deviceMatch}, activityId = ${activityId}, ctNum = ${ctNum}, ctUnit = ${ctUnit} "
                                device = device.label
                                delay = true
                                data = [type: "cHarmony", command: command, device: device, unit: activityId, num: ctNum, delay: delay]
                                log.warn "delay Harmony with data: ${data}"
                                runIn(ctNum*60, controlHandler, [data: data])
                                outputTxt = "Ok, turning " +  deviceMatch + " activity " + command + ", in " + numText
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            else{                        
                            delay = false
                            data = [type: "cHarmony", command: command, device: device, unit: activityId, num: ctNum, delay: delay]
                            outputTxt = controlHandler(data)
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                        }
                        //Switch Control
                        if (deviceMatch && dType == "s") {
                            device = deviceMatch
                            if (command == "disable" || command == "deactivate"|| command == "stop") {command = "off"}
                            if (command == "enable" || command == "activate"|| command == "start") {command = "on"}    
                            if (ctNum > 0 && ctUnit == "minutes") {
                                device = device.label
                                delay = true
                                data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                runIn(ctNum*60, controlHandler, [data: data])
                                if (command == "on" || command == "off" ) {outputTxt = "Ok, turning " + ctDevice + " " + command + ", in " + numText}
                                else if (command == "decrease") {outputTxt = "Ok, decreasing the " + ctDevice + " level in " + numText}
                                else if (command == "increase") {outputTxt = "Ok, increasing the " + ctDevice + " level in " + numText}
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            else {
                                delay = false
                                data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                outputTxt = controlHandler(data)
                                if (command == "decrease" || command == "increase") {state.pContCmdsR = "level"}
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                        }         
            }
    // >>>> THERMOSTAT CONTROL <<<<
            else if (deviceType == "temp") {
                    if (settings.cTstat?.size() > 0) {           
                        def deviceMatch = cTstat.find {t -> t.label.toLowerCase() == ctDevice.toLowerCase()}
                        if (deviceMatch) {
                            device = deviceMatch 
                            if(state.usePIN_T == true) { // (THIS PIN VALIDATION HAS BEEN Deprecated as of 1/23/2017)
                                if (debug) log.warn "PIN protected device type - '${deviceType}'"
                                delay = false
                                data = ["type": "cTstat", "command": command , "device": ctDevice, "unit": ctUnit, "num": ctNum, delay: delay]
                                state.savedPINdata = data
                                outputTxt = "Pin number please"
                                pPIN = true
                                state.pinTry = 0
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            else {                       
                                if (ctNum && ctUnit == "minutes") {
                                    delay = true
                                    data = [type: "cTstat", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                    runIn(ctNum*60, delayHandler, [data: data])
                                    if (command == "decrease") {outputTxt = "Ok, decreasing the " + ctDevice + " temperature in " + numText}
                                    else if (command == "increase") {outputTxt = "Ok, increasing the " + ctDevice + " temperature in " + numText}
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                                else {
                                    delay = false
                                    data = [type: "cTstat", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                    outputTxt = controlHandler(data)
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                            }
                       }
                    }
             }
    // >>>> LOCKS CONTROL <<<<
            else if (deviceType == "lock") {
                if (settings.cLock?.size()>0) {   
                    def deviceMatch = cLock.find {l -> l.label.toLowerCase() == ctDevice.toLowerCase()}             
                    if (deviceMatch) {
                        device = deviceMatch
                        if(state.usePIN_L == true) { // (THIS PIN VALIDATION HAS BEEN Deprecated as of 1/23/2017)
                            if (debug) log.warn "PIN protected device type - '${deviceType}'"               		
                            delay = false
                            data = [type: "cLock", "command": command , "device": ctDevice, "unit": ctUnit, "num": ctNum, delay: delay]
                            state.savedPINdata = data
                            outputTxt = "Pin number please"
                            pPIN = true
                            state.pinTry = 0
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
                        else {
                            if (ctNum > 0 && ctUnit == "minutes") {
                                device = device.label
                                delay = true
                                data = [type: "cLock", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                runIn(ctNum*60, controlHandler, [data: data])
                                if (command == "lock") {outputTxt = "Ok, locking the " + ctDevice + " in " + numText}
                                else if (command == "unlock") {outputTxt = "Ok, unlocking the " + ctDevice + " in " + numText}
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            else {
                                delay = false
                                data = [type: "cLock", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                outputTxt = controlHandler(data)
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                        }
                    }
                }
            }
    // >>>> FANS CONTROL <<<<        
            else if (deviceType == "fan") {
                if (settings.cFan?.size()>0) {     
                    def deviceMatch = cFan.find {f -> f.label.toLowerCase() == ctDevice.toLowerCase()}
                    if (deviceMatch) {
                            device = deviceMatch
                            if (ctNum && ctUnit == "minutes") {
                                delay = true
                                data = [type: "cFan", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                runIn(ctNum*60, delayHandler, [data: data])
                                if (command == "decrease") {outputTxt = "Ok, decreasing the " + ctDevice + " temperature in " + numText}
                                else if (command == "increase") {outputTxt = "Ok, increasing the " + ctDevice + " temperature in " + numText}
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            else {
                                delay = false
                                data = [type: "cFan", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                outputTxt = controlHandler(data)
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                    }
                }
            }
    // >>>> DOORS CONTROL <<<<        
            else if (deviceType == "door") {
                if (settings.cDoor?.size()>0) {          
                    def deviceMatch = cDoor.find {d -> d.label.toLowerCase() == ctDevice.toLowerCase()}
                    if (deviceMatch) {
                        device = deviceMatch
                    //Check Status
                        def deviceR = device.label
                        def cDoorStatus = device.contactState.value
                            if (command == "open" && cDoorStatus == "open") {
                            outputTxt = "The " + device + " is already open, would you like to close it instead?"
                            state.pContCmdsR = "door"
                            def actionData = ["type": "cDoor", "command": "close" , "device": deviceR, "unit": ctUnit, "num": ctNum, delay: delayD]
                            state.lastAction = actionData
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
                        if (command == "close" && cDoorStatus =="closed") {
                            outputTxt = "The " + device + " is already closed, would you like to open it instead? "
                            state.pContCmdsR = "door"
                            def actionData = ["type": "cDoor", "command": "open" , "device": deviceR, "unit": ctUnit, "num": ctNum, delay: delayD]
                            state.lastAction = actionData
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
                        if(state.usePIN_D == true) {
                            //PIN VALIDATION PROCESS (Deprecated code as of 1/23/2017)
                            if (debug) log.warn "PIN protected device type - '${deviceType}'"
                            delay = false
                            data = [type: "cDoor", "command": command , "device": ctDevice, "unit": ctUnit, "num": ctNum, delay: delay]
                            state.savedPINdata = data
                            outputTxt = "Pin number please"
                            pPIN = true
                            state.pinTry = 0
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
                        else {                 
                            if (ctNum && ctUnit == "minutes") {
                                delay = true
                                data = [type: "cDoor", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                runIn(ctNum*60, delayHandler, [data: data])
                                if (command == "open") {outputTxt = "Ok, opening " + ctDevice + " in " + numText}
                                else if (command == "close") {outputTxt = "Ok, closing " + ctDevice + " in " + numText}
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            else {
                                delay = false
                                data = [type: "cDoor", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                outputTxt = controlHandler(data)
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                        }
                    }
                }
    	// >>>> RELAYS CONTROL <<<<            
                if (cRelay !=null) {
                //this is needed for Garage Doors that are set up as relays
                    def deviceMatch = cRelay.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}             
                    if (deviceMatch) {
                        device = deviceMatch
                        if (cContactRelay) {
                            if (debug) log.debug "Garage Door has a contact sensor"
                            def deviceR = device.label
                             def cCRelayValue = cContactRelay.contactState.value
                                if (command == "open" && cCRelayValue == "open") {
                                    outputTxt = "The " + device + " is already open, would you like to close it instead?"
                                    state.pContCmdsR = "door"
                                    def actionData = ["type": "cRelay", "command": "close" , "device": deviceR, "unit": unitU, "num": newLevel, delay: delayD]
                                    state.lastAction = actionData
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                                if (command == "close" && cCRelayValue =="closed") {
                                    outputTxt = "The " + device + " is already closed, would you like to open it instead? "
                                    state.pContCmdsR = "door"
                                    def actionData = ["type": "cRelay", "command": "open" , "device": deviceR, "unit": ctUnit, "num": ctNum, delay: delay]
                                    state.lastAction = actionData
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                 }
                             }
                            //PIN VALIDATION PROCESS (Deprecated code as of 1/23/2017)
                            if(state.usePIN_D == true) {
                                if (debug) log.warn "PIN protected device type - '${deviceType}'"
                                delay = false
                                data = [type: "cRelay", "command": command , "device": ctDevice, "unit": ctUnit, "num": ctNum, delay: delay]
                                state.savedPINdata = data
                                outputTxt = "Pin number please"
                                pPIN = true
                                state.pinTry = 0
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            else {                 
                            //END PIN VALIDATION                                       
                                if (ctNum > 0 && ctUnit == "minutes") {
                                    device = device.label
                                    delay = true
                                    data = [type: "cRelay", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                    runIn(ctNum*60, controlHandler, [data: data])
                                    if (ctCommand == "open") {outputTxt = "Ok, opening the " + ctDevice + " in " + numText}
                                    else if (command == "close") {outputTxt = "Ok, closing the " + ctDevice + " in " + numText}
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                                else {
                                    delay = false
                                    data = [type: "cRelay", "command": command, "device": device, unit: ctUnit, num: ctNum, delay: delay]
                                    outputTxt = controlHandler(data)
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                               }
                         }
                    }
                }
    	// >>>> VENTS CONTROL <<<<            
                if (settings.cVent?.size()>0) {
                //this is needed to enable open/close command for Vents group
                    def deviceMatch = cVent.find {s -> s.label.toLowerCase() == ctDevice.toLowerCase()}             
                    if (deviceMatch) {
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
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            else {
                                delay = false
                                data = [type: "cSwitch", command: command, device: device, unit: ctUnit, num: ctNum, delay: delay]
                                controlHandler(data)
                                if (ctCommand == "open") {outputTxt = "Ok, opening the " + ctDevice}
                                else if (ctCommand == "close") {outputTxt = "Ok, closing the " + ctDevice}
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                    }
                }
            }
            def hText = ctDevice != "undefined" && ctCommand != "undefined" ? ctCommand + " the " + ctDevice :  ctDevice != "undefined" ? " control " + ctDevice : ctCommand != "undefined" ? ctCommand + " something" : "control something" 
			def sText = ctDevice != "undefined" && ctCommand != "undefined" ? "the command " + ctCommand + " and device " + ctDevice : ctDevice != "undefined" ? " device named " + ctDevice : ctCommand != "undefined" ? " command named " + ctCommand : " something " 
            if (state.pShort != true){ 
            		outputTxt = "Sorry, I heard that you were looking to " + hText + " but Echosistant wasn't able to take any actions "
                }
                else {outputTxt = "I've heard " + sText +  " but I wasn't able to take any actions "} 
            state.pTryAgain = true
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        }
    	outputTxt = "Sorry, I didn't get that, "
		state.pTryAgain = true
		return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    }
       } catch (Throwable t) {
        log.error t
        outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
	}
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
    def actionData
    if (debug) log.debug 	"Received device control handler data: " +
        					" (deviceType)= ${deviceType}',(deviceCommand) = '${deviceCommand}', (deviceD) = '${deviceD}', " +
                            "(unitU) = '${unitU}', (numN) = '${numN}', (delayD) = '${delayD}'"  

	state.pTryAgain = false
	if (deviceType == "cSwitch" || deviceType == "cMiscDev"  ) {
    	if (deviceCommand == "on" || deviceCommand == "off") {
            if (delayD == true ) {
                if(deviceType == "cSwitch") {deviceD = cSwitch.find {s -> s.label.toLowerCase() == deviceD.toLowerCase()}}
                if (deviceType == "cMiscDev") {
                	deviceD = cMiscDev.find {s -> s.label.toLowerCase() == deviceD.toLowerCase()}            
                	deviceD."${deviceCommand}"()
                    result = "Ok, turning " + deviceD + " " + deviceCommand 
                	return result          
				}
                else {
                	deviceD."${deviceCommand}"()
                }
			}
            else {
            	deviceD."${deviceCommand}"()            	
                result = "Ok, turning " + deviceD + " " + deviceCommand 
                return result
            }
        }
        else if (deviceCommand == "onD") {
        		deviceD.on()
                deviceD.setLevel(100)
                
        }
        else if (deviceCommand == "offD") {
        		deviceD.off()
        }        
        else if (deviceCommand == "increase" || deviceCommand == "decrease" || deviceCommand == "setLevel" || deviceCommand == "set") {
 			if (delayD == true) {
            	if(deviceType == "cSwitch") {deviceD = cSwitch.find {s -> s.label.toLowerCase() == deviceD.toLowerCase()}}
                else if (deviceType == "cMiscDev") {deviceD = cMiscDev.find {s -> s.label.toLowerCase() == deviceD.toLowerCase()}}            
            }            
            if(state.pContCmdsR == "repeat") {state.pContCmdsR == "level"}
            def currLevel = deviceD.latestValue("level")
            def currState = deviceD.latestValue("switch")
            def newLevel = cLevel*10
            if (unitU == "percent") newLevel = numN      
            if (deviceCommand == "increase") {
            	if (unitU == "percent") {
                	newLevel = numN
                }   
                else {
                	if (currLevel == null){
                    deviceD.on()
                    result = "Ok, turning " + deviceD + " on"
            		return result    
                    }
                    else {
                	newLevel =  currLevel + newLevel
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
            		}
                }
            }
            if (deviceCommand == "decrease") {
            	if (unitU == "percent") {
                	newLevel = numN
                }   
                else {
                	if (currLevel == null) {
                    deviceD.off()
                    result = "Ok, turning " + deviceD + " off"
            		return result                    
                    }
                    else {
                	newLevel =  currLevel - newLevel
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                    }
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
            def device = deviceD.label
            def delayL = true
            actionData = ["type": deviceType, "command": deviceCommand , "device": device, "unit": unitU, "num": newLevel, delay: delayL]
            state.lastAction = actionData
            result = "Ok, setting  " + deviceD + " to " + newLevel + " percent"            
            if (delayD == false || deviceType == "cMiscDev"  || state.pContCmdsR == "repeat") { return result } 
    	}
	}
	else if (deviceType == "cTstat") {
 		if (delayD == true || state.pinTry != null) {  
                deviceD = cTstat.find {t -> t.label.toLowerCase() == deviceD.toLowerCase()} 
        }
        state.pinTry = null
    	def currentMode = deviceD.latestValue("thermostatMode")
    	def currentHSP = deviceD.latestValue("heatingSetpoint") 
        def currentCSP = deviceD.latestValue("coolingSetpoint") 
    	def currentTMP = deviceD.latestValue("temperature") 
    	def newSetPoint = currentTMP
		numN = numN < 60 ? 60 : numN >85 ? 85 : numN
        if (unitU == "degrees") {
    		newSetPoint = numN
            int cNewSetPoint = newSetPoint
    		if (newSetPoint > currentTMP) {
    			if (currentMode == "off") { // currentMode == "cool" || removed so it does't change Modes as frequently 2/16/2017
    				deviceD?."heat"()
					if (debug) log.trace 	"Turning heat on because requested temperature of '${newSetPoint}' "+
                    						"is greater than current temperature of '${currentTMP}' " 
    			}
				deviceD?.setHeatingSetpoint(newSetPoint)
                result = "Ok, setting " + deviceD + " heating to " + cNewSetPoint 
                    if (delayD == false) { 
                    	state.pinTry = null
                    	return result 
                    }
            }
 			else if (newSetPoint < currentTMP) {
				if (currentMode == "off") { //currentMode == "heat" || removed so it does't change Modes as frequently 2/16/2017
					deviceD?."cool"()
					if (debug) log.trace "Turning AC on because requested temperature of '${newSetPoint}' is less than current temperature of '${currentTMP}' "    
				}
				deviceD?.setCoolingSetpoint(newSetPoint)                 
				if (debug) log.trace "Adjusting Cooling Set Point to '${newSetPoint}' because requested temperature is less than current temperature of '${currentTMP}'"
                result = "Ok, setting " + deviceD + " cooling to " + cNewSetPoint + " degrees "
                    if (delayD == false) { 
                    	return result 
                    }                       
            }
            else result = "Your room temperature is already " + cNewSetPoint + " degrees "
                    if (delayD == false) { 
                    	return result 
                    }
		}
		if (deviceCommand == "increase") {
			newSetPoint = currentTMP + cTemperature
			newSetPoint = newSetPoint < 60 ? 60 : newSetPoint >85 ? 85 : newSetPoint
            int cNewSetPoint = newSetPoint
			if (currentMode == "cool" || currentMode == "off") {
				deviceD?."heat"()
                deviceD?.setHeatingSetpoint(newSetPoint)
                if (debug) log.trace "Turning heat on because requested command asked for heat to be set to '${newSetPoint}'"
                result = "Ok, turning the heat mode on " + deviceD + " and setting heating to " + cNewSetPoint + " degrees "
                return result 
			}
			else {
				if  (currentHSP < newSetPoint) {
					deviceD?.setHeatingSetpoint(newSetPoint)
					thermostat?.poll()
					if (debug) log.trace "Adjusting Heating Set Point to '${newSetPoint}'"
                    result = "Ok, setting " + deviceD + " heating to " + cNewSetPoint + " degrees "
                        if (delayD == false) { 
                            return result 
                        }    
                }
                else {
                   	if (debug) log.trace "Not taking action because heating is already set to '${currentHSP}', which is higher than '${newSetPoint}'" 
                    result = "Your heating set point is already higher than  " + cNewSetPoint + " degrees "
                    if (delayD == false) { 
                    	return result 
                    }    
               	}  
            }
       	}
        if (deviceCommand == "decrease") {
        	newSetPoint = currentTMP - cTemperature
        	newSetPoint = newSetPoint < 60 ? 60 : newSetPoint >85 ? 85 : newSetPoint     
            int cNewSetPoint = newSetPoint
            if (currentMode == "heat" || currentMode == "off") {
        		deviceD?."cool"()
                deviceD?.setCoolingSetpoint(newSetPoint)
        		if (debug) log.trace "Turning AC on because requested command asked for cooling to be set to '${newSetPoint}'"
                result = "Ok, turning the AC mode on " + deviceD + " and setting cooling to " + cNewSetPoint + " degrees "
                return result                 
        	}   	
        	else {
        		if (currentCSP > newSetPoint) {
        			deviceD?.setCoolingSetpoint(newSetPoint)
        			thermostat?.poll()
        			if (debug) log.trace "Adjusting Cooling Set Point to '${newSetPoint}'"
        			result = "Ok, setting " + deviceD + " cooling to " + cNewSetPoint + " degrees "
                    if (delayD == false) { 
                    	return result 
                    }
                }
        		else {
        			if (debug) log.trace "Not taking action because cooling is already set to '${currentCSP}', which is lower than '${newSetPoint}'"  
                    result = "Your cooling set point is already lower than  " + cNewSetPoint + " degrees "
                    if (delayD == false) { 
                    	return result 
                    }
                } 
        	}  
        }
    }
	else if (deviceType == "cLock") {
    	if (delayD == true || state.pinTry != null) {  
        	deviceD = cLock.find {l -> l.label.toLowerCase() == deviceD.toLowerCase()} 
        }
        state.pinTry = null
   		deviceD."${deviceCommand}"()
        if (deviceCommand == "lock") result = "Ok, locking " + deviceD
        else if (deviceCommand == "unlock") result = "Ok, unlocking the  " + deviceD                    
        if (delayD == false) {return result}  
	}
    else if (deviceType == "cDoor" || deviceType == "cRelay" ) {
    	def cmd = deviceCommand
        log.warn "pinTry = ${state.pinTry}"
        if (delayD == true || state.pinTry != null || state.pContCmdsR == "door" ) {  
            def deviceR = cRelay.find {r -> r.label.toLowerCase() == deviceD.toLowerCase()}
			deviceD = cDoor.find {d -> d.label.toLowerCase() == deviceD.toLowerCase()}   
            if (deviceR) {deviceD = deviceR}
            
        }
        if (deviceType == "cRelay") {
		     cmd = "on"
        }
        state.pinTry = null
        deviceD."${cmd}"()
        state.pContCmdsR = null //"reverse"
        if (deviceCommand == "open") {result = "Ok, opening the " + deviceD}
        if (deviceCommand == "close") {result = "Ok, closing the  " + deviceD}                   
        if (delayD == false || delayD == null) {return result}  
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
                deviceD.setLevel(newLevel)
                result = "Ok, increasing  " + deviceD + " to " + newLevel + " percent"
       				if (delayD == false) { return result }
            }
            else if (deviceCommand == "decrease") {
               	newLevel =  currLevel - newLevel
            	newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                deviceD.setLevel(newLevel)
                result = "Ok, decreasing  " + deviceD + " to " + newLevel + " percent"
       				if (delayD == false) { return result }        
            }
            else {
                if (deviceCommand == "high") {newLevel = cHigh}
                if (deviceCommand == "medium") {newLevel = cMedium}
                if (deviceCommand == "low") {newLevel = cLow}
                    deviceD.setLevel(newLevel)
                    result = "Ok, setting  " + deviceD + " to " + newLevel + " percent"
                    if (delayD == false) {return result} 
           }           
	}
	if (deviceType == "cVolume" || deviceType == "cHarmony"  ) {
   		if (deviceCommand == "increase" || deviceCommand == "decrease" || deviceCommand == "setLevel" || deviceCommand == "mute" || deviceCommand == "unmute"){
            def currLevel = deviceD.latestValue("level")
            def currState = deviceD.latestValue("switch")
            if (cVolLevel == null) {cVolLevel = 2}
            def newLevel = cVolLevel*10
			if (unitU == "percent") newLevel = numN      
            if (deviceCommand == "mute" || deviceCommand == "unmute") {
				deviceD."${deviceCommand}"()
            	def volText = deviceCommand == "mute" ? "muting" : deviceCommand == "unmute" ? "unmuting" : "adjusting" 
                result = "Ok, " + volText + " the " + deviceD
            	return result
            }
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
		//HARMONY ACTIONS
			if (deviceCommand == "start" || deviceCommand == "switch" || deviceCommand == "on" || deviceCommand == "off" || deviceCommand == "end" || deviceCommand == "set" ) {
                if(deviceType == "cHarmony") {     	
                	if(delayD == true){deviceD = cMedia.first()} 
                    	if (deviceCommand == "start" || deviceCommand == "switch" || deviceCommand == "on" || deviceCommand == "set"){
							deviceCommand = "startActivity"
                            if (unitU != "undefined"){
                            	log.warn "starting unitU = ${unitU}"
                            	deviceD."${deviceCommand}"(unitU)
                        		deviceD.refresh() 
                        		if(debug) log.debug "starting - deviceD: ${deviceD.label}, deviceCommand:${deviceCommand}, unitU:${unitU}"
                                result = "Ok, starting " + deviceD + " activity"
                        		return result
                            }
                            else {
                                if(state.lastActivity != null){
                                    def activityId = null
                                    def sMedia = cMedia.first()
                                    def activities = sMedia.currentState("activities").value
                                    def activityList = new groovy.json.JsonSlurper().parseText(activities)
                                    activityList.each { it ->  
                                        def activity = it
                                        if(activity.name == state.lastActivity) {
                                            activityId = activity.id
                                        }    	
                                    }
                                    deviceD."${deviceCommand}"(activityId)
                                    deviceD.refresh() 
                                    result = "Ok, starting " + deviceD + " activity"
                                    return result
                                }
                                else { 
                                    if(debug) log.warn "last activity must be saved - deviceD: ${deviceD.label}, deviceCommand:${deviceCommand}, activityId:${activityId}"
                                    result = "Sorry for the trouble, but in order for EchoSistant to be able to start where you left off, the last activity must be saved"
                                    return result
                                }
                         	}
                    }
                    else{
                    	def activityId = null
                        def currState = deviceD.currentState("currentActivity").value
						def activities = deviceD.currentState("activities").value
						def activityList = new groovy.json.JsonSlurper().parseText(activities)
                		if (currState != "--"){
							activityList.each { it ->  
								def activity = it
                                if(activity.name == currState) {
                                    activityId = activity.id
								}    	
                            }
                        	deviceCommand = "activityoff"
							state.lastActivity = currState
                            //deviceCommand =  "alloff" // 2/10/2017 changed to turn off current activity to avoid turning OFF all hubs
							if(debug) log.debug "ending - deviceD: ${deviceD.label}, deviceCommand:${deviceCommand}, activityId:${activityId}"
                            //deviceD."${deviceCommand}"(activityId)
                            deviceD."${deviceCommand}"()
                            deviceD.refresh()
                        	result = "Ok, turning off " + currState
                        	return result
                        }
                        else {
                        	result = "${deviceD} is already off"
                            state.pTryAgain = true
                            return result
                        }
                   }
                }
            }
       }
    }
}
/************************************************************************************************************
   SECURITY CONTROL - from Lambda via page s
************************************************************************************************************/
def controlSecurity() {
		//FROM LAMBDA
        def command = params.sCommand
        def num = params.sNum
        def sPIN = params.sPIN
        def type = params.sType
        def control = params.sControl       
		def pintentName = params.intentName
        //OTHER VARIABLES
        def String outputTxt = (String) null 
		def pPIN = false
        def String secCommand = (String) null
        def delay = false
        def data = [:]
			control = control.replaceAll("[^a-zA-Z0-9]", "")
        	sPIN = sPIN == "?" ? "undefined" : sPIN
        if (num == "undefined" || num =="?") {num = 0 } 
        	num = num as int
        
        if (debug) log.debug "System Control Data: (sCommand)= ${command},(sNum) = '${num}', (sPIN) = '${sPIN}'," +
        					 " (type) = '${type}', (sControl) = '${control}',(pintentName) = '${pintentName}'"
	def sProcess = true
    state.pTryAgain = false
try {	
	if (pintentName == "security") {    
        def modes = location.modes.name
    	def currMode = location.currentMode
    	def routines = location.helloHome?.getPhrases()*.label
    	def currentSHM = location.currentState("alarmSystemStatus")?.value
    	// HANDLING SHM
        if (type != "mode") { 
            if (control == "status") {      
                    currentSHM = currentSHM == "off" ? "disabled" : currentSHM == "away" ? "Armed Away" : currentSHM == "stay" ? "Armed Stay" : "unknown"
                    outputTxt = "Your Smart Home Monitor Status is " +  currentSHM
                    state.pTryAgain = false
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }
            if (command == "cancel" || command == "stop" || command == "disable" || command == "deactivate" || command == "off" || command == "disarm") {
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
                    outputTxt = "Ok, changing the Smart Home Monitor to armed stay in " + numText.text
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
					def mMatch = m.replaceAll("[^a-zA-Z0-9 ]", "")
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
            	def rMatch = r.replaceAll("[^a-zA-Z0-9 ]", "")
            	if(rMatch.toLowerCase() == control.toLowerCase()){
                   		if(cPIN && cRoutines) {
                         	def pinRoutine =  cRoutines.find {r1 -> r1 == r}  
                            if (pinRoutine) {
                                delay = false
                                data = [command: r, delay: delay]
                                state.lastAction = data
                                state.pContCmdsR = "mode"
                                //RUN PIN VALIDATION PROCESS
                                def pin = "undefined"
                                command = "validation"
                                def unit = "routine"
                                outputTxt = pinHandler(pin, command, num, unit)
                                pPIN = true
                                if (state.pinTry == 3) {pPIN = false}
                                log.warn "try# ='${state.pinTry}'"                        
                            }
                            else { 
                                location.helloHome?.execute(r)
                                outputTxt = "Ok, I am running the " + control + " routine"
                            }
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
			else {outputTxt = "I've heard " + sText +  " but I wasn't able manage your system controls "} 
            state.pTryAgain = true
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    }
    } catch (Throwable t) {
        log.error t
        outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
	}
}
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
	try {
        if(ptts == "no" || ptts == "stop" || ptts == "cancel" || ptts == "kill it" || ptts == "zip it" || ptts == "yes" && state.pContCmdsR != "wrongIntent"){
        	if(ptts == "no" || ptts == "stop" || ptts == "cancel" || ptts == "kill it" || ptts == "zip it"){
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
                    dataSet = [ptts:ptts, pintentName:pintentName] 
                    def pResponse = child.profileEvaluate(dataSet)
                    outputTxt = pResponse?.outputTxt
                    pContCmds = pResponse?.pContCmds
                    pContCmdsR = pResponse?.pContCmdsR
                    pTryAgain = pResponse?.pTryAgain
                }
            }
            if (outputTxt?.size()>0){
                return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]
            }
            else {
                if (state.pShort != true){
                	outputTxt = "I wish I could help, but EchoSistant couldn't find a Profile named " + pintentName + " or the command may not be supported"
                }
                else {outputTxt = "I've heard " + pintentName + " , but I wasn't able to take any actions "} 
                pTryAgain = true
                return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain": pTryAgain, "pPIN":pPIN]
            }
        def hText = "run a messaging and control profile"
			if (state.pShort != true){ 
				outputTxt = "Sorry, I heard that you were looking to " + hText + " but Echosistant wasn't able to take any actions "
			}
			else {outputTxt = "I've heard " + pintentName + " , but I wasn't able to take any actions "}         
			pTryAgain = true
			return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":pTryAgain, "pPIN":pPIN]              
    	}

} catch (Throwable t) {
        log.error t
        outputTxt = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return ["outputTxt":outputTxt, "pContCmds":pContCmds, "pShort":state.pShort, "pContCmdsR":pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
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
			if (evt.value == "away") {
            	sendAwayCommand
            	if(shmSynthDevice) shmSynthDevice?.speak("Attention, The alarm system has changed status to armed '${curEvtValue}'")
            	if (shmSonosDevice) 
             	shmSonosDevice?.playTextAndRestore("Attention, The alarm system has changed status to armed '${curEvtValue}'")
            	}
                else if (evt.value == "stay") {
                	if(shmSynthDevice) shmSynthDevice?.speak("Attention, The alarm system has changed status to armed '${curEvtValue}'")
            		if (shmSonosDevice) 
             		shmSonosDevice?.playTextAndRestore("Attention, The alarm system has changed status to armed '${curEvtValue}'")
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
/******************************************************************************
	 FEEDBACK SUPPORT - GET AVERAGE										
******************************************************************************/
def getAverage(device,type){
	def total = 0
		if(debug) log.debug "calculating average temperature"  
    device.each {total += it.latestValue(type)}
    return Math.round(total/device.size())
}
/******************************************************************************
	 FEEDBACK SUPPORT - ADDITIONAL FEEDBACK											
******************************************************************************/
def getMoreFeedback(data) {
    def devices = data.devices
    def deviceType = data.deviceType
    def command = data.cmd
    def result 
	if ( deviceType == "cSwitch") {
    	result = "The following switches are " + command + "," + devices.sort().unique()
    }
	if ( deviceType == "cContact") {
    	result = "The following doors or windows are " + command + "," + devices.sort().unique()
    }
    state.pContCmdsR = null 
	state.lastAction = null
    return result
}
/******************************************************************************
	 FEEDBACK SUPPORT - DEVICE MATCH											
******************************************************************************/
private deviceMatchHandler(fDevice) {
    def pPIN = false
    def String deviceType = (String) null
	def currState
    def stateDate
    def stateTime
	def deviceMatch
    def result
    	state.pTryAgain = false
		if(cTstat){
           deviceMatch = cTstat?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
			if(deviceMatch){
				deviceType = "cTstat"
                currState = deviceMatch.currentState("thermostatOperatingState").value
                stateDate = deviceMatch.currentState("thermostatOperatingState").date
                stateTime = deviceMatch.currentState("thermostatOperatingState").date.time
                def timeText = getTimeVariable(stateTime, deviceType)            
            	return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "thermostatOperatingState" ]
            }
        }
        if (cSwitch){
		deviceMatch = cSwitch?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
			if(deviceMatch){
				deviceType = "cSwitch" 
				currState = deviceMatch.currentState("switch").value
				stateDate = deviceMatch.currentState("switch").date
				stateTime = deviceMatch.currentState("switch").date.time
				def timeText = getTimeVariable(stateTime, deviceType)
            	return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "switch"]
        	}
        }
        if (cContact){
        deviceMatch =cContact?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cContact" 
				currState = deviceMatch.currentState("contact").value
				stateDate = deviceMatch.currentState("contact").date
				stateTime = deviceMatch.currentState("contact").date.time
				def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "contact"]
            }
        }
        if (cMotion){
        deviceMatch =cMotion?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cMotion" 
                currState = deviceMatch.currentState("motion").value 
                stateDate = deviceMatch.currentState("motion").date
                stateTime = deviceMatch.currentState("motion").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "motion"]
        	}
        } 
        if (cLock){
        deviceMatch =cLock?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cLock"
                currState = deviceMatch.currentState("lock").value 
                stateDate = deviceMatch.currentState("lock").date
                stateTime = deviceMatch.currentState("lock").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "lock"]
        	}
        }        
        if (cPresence){
        deviceMatch =cPresence?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cPresence"
                currState = deviceMatch.currentState("presence").value 
                stateDate = deviceMatch.currentState("presence").date
                stateTime = deviceMatch.currentState("presence").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, , "mainCap": "presence"]
        	}
        }  
        if (cDoor){
        deviceMatch =cDoor?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cDoor"
                currState = deviceMatch.currentState("door").value 
                stateDate = deviceMatch.currentState("door").date
                stateTime = deviceMatch.currentState("door").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "door"]
        	}
        }  
        if (cVent){
		deviceMatch =cVent?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cVent"
                currState = deviceMatch.currentState("switch").value 
                currState = currState == "on" ? "open" : currState == "off" ? "closed" : "unknown"
                stateDate = deviceMatch.currentState("switch").date
                stateTime = deviceMatch.currentState("switch").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, , "mainCap": "switch"]
        	}
        }
        if (cWater){
		deviceMatch =cWater?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cWater"
                currState = deviceMatch.currentState("water").value 
                stateDate = deviceMatch.currentState("water").date
                stateTime = deviceMatch.currentState("water").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText,  "mainCap": "water"]
        	}
        }        
        if (cMedia){
            if (fDevice == "TV") {
                deviceMatch = cMedia.first()
            }
            else {
                deviceMatch =cMedia?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            }   
            if(deviceMatch)	{
                deviceType = "cMedia"
                currState = deviceMatch.currentState("currentActivity").value 
                currState = currState == "--" ? " off " : " running the " + currState + " activity "
                stateDate = deviceMatch.currentState("currentActivity").date
                stateTime = deviceMatch.currentState("currentActivity").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText,  "mainCap": "currentActivity"]
            }
        }        
        if (cFan){
		deviceMatch =cFan?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cFan"
                currState = deviceMatch.currentState("switch").value 
                stateDate = deviceMatch.currentState("switch").date
                stateTime = deviceMatch.currentState("switch").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText, "mainCap": "switch"] 
            }
        }         
        if (cRelay){
		deviceMatch =cRelay?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
				deviceType == "cRelay"
                if (cContactRelay) {
                currState = cContactRelay.currentState("contact").value 
                stateDate = cContactRelay.currentState("contact").date
                stateTime = cContactRelay.currentState("contact").date.time
                def timeText = getTimeVariable(stateTime, deviceType)
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": currState, "tText": timeText.tText,  "mainCap": "contact"] 
                }
			}
        }
        if (cBattery){
        deviceMatch =cBattery?.find {d -> d.label.toLowerCase() == fDevice.toLowerCase()}
            if(deviceMatch)	{
                deviceType = "cBattery"
                currState = cBattery.currentState("battery").value
				stateTime = cBattery.currentState("battery").date.time
                def timeText = getTimeVariable(stateTime, deviceType)  
                return ["deviceMatch" : deviceMatch, "deviceType": deviceType, "currState": "", "tText": timeText.tText,  "mainCap": "battery"]
            } 
     	}
}
/******************************************************************************
	 FEEDBACK SUPPORT - DEVICE CAPABILITIES											
******************************************************************************/
private getCaps(capDevice,capType, capMainCap, capState) {
    def deviceName = capDevice
    def deviceType = capType
    def deviceCap = capMainCap
    def deviceState = capState
    def result
    def attr = [:]
    	state.pContCmdsR = "caps"
	def supportedCaps = deviceName.capabilities
    supportedCaps.each { c ->
		def capName = c.name
            c.attributes.each {a ->
        		def attrName = a.name
                 def attrValue = deviceName.latestValue(attrName)               
                 if (a.name != null && a.name !=checkInterval && a.name !=polling  && a.name !=refresh && attrValue != null ) {
                    if (a.name == "temperature") 		{ result = "The " + attrName + " is " + attrValue + " degrees, " }
                    if (a.name == "motion") 			{ result = result + attrName + " is " + attrValue +", " }
                    if (a.name == "contact") 			{ result = result + attrName + " is " + attrValue +", " }                    
                    if (a.name == "humidity") 			{ result = result + attrName + " is " + attrValue + ", " }
                    if (a.name == "illuminance") 		{ result = result + "lux level is " + attrValue + ", " }
                    if (a.name == "water") 				{ result = result + attrName + " is " + attrValue +", " }                    
                    if (a.name == "switch") 			{ result = result + attrName + " is " + attrValue +", " } 
					if (a.name == "presence") 			{ result = result + attrName + " is " + attrValue + ", " }                    
                    if (a.name == "heatingSetpoint") 	{ result = result + "Heating Set Point is " + attrValue + " degrees, " }
                    if (a.name == "coolingSetpoint") 	{ result = result + "Cooling Set Point is" + attrValue + " degrees, " }
                    if (a.name == "thermostatMode") 	{ result = result + "The thermostat Mode is " + attrValue + ", " }
                    if (a.name == "thermostatFanMode") 	{ result = result + "The Fan Mode is " + attrValue + ", " }
					if (a.name == "battery") 			{ result = result + attrName + " level is " + attrValue + " percent, " }
                        attr << ["${attrName}": attrValue]
            	}
           }
     }
	result = result.replace("null", "")
    state.lastAction = result
    state.pContCmdsR = "caps"
    result = attr.size()
    return result
}
/******************************************************************************
	 FEEDBACK SUPPORT - CAPABILITIES GROUP											
****************************************************************************/
private getCapabilities(cap) {
    def DeviceDetails = [] 
    def batDetails = [] 
    def result = [:] 
    	state.pTryAgain = false	
try {
//batteries
	if (cap == "bat") {
        cMotion?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < cLowBattery) {
        		batDetails << d.displayName         
             }     
         }
        cContact?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < cLowBattery) {
        		batDetails << d.displayName         
             }
         }
         
        cPresence?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < cLowBattery) {
        		batDetails << d.displayName         
             }
         }
        cWater?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < cLowBattery) {
        		batDetails << d.displayName         
             }
         }
        cVent?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < cLowBattery) {
        		batDetails << d.displayName        
             }
         }
        cLock.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < cLowBattery) {
        		batDetails << d.displayName         
             }
         }    
        cBattery?.each 	{ d ->
        def attrValue = d.latestValue("battery") 
        	if (attrValue < cLowBattery) {
        		batDetails << d.displayName     
             }
         }
        def dUniqueList = batDetails.unique (false)
        dUniqueList = dUniqueList.sort()       
        def listSize = dUniqueList.size()
        def listBat = dUniqueList
        result = [listSize: listSize, listBat: listBat]
        return result //dUniqueListString
	}
//activity	
    if (cap == "act") {
        cMotion?.each 	{ d ->
        	def stateTime = d.currentState("motion").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > cInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        cContact?.each 	{ d ->
        def attrValue = d.latestValue("contact") 
        	def stateTime = d.currentState("contact").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > cInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        cPresence?.each 	{ d ->
        def attrValue = d.latestValue("presence") 
        	def stateTime = d.currentState("presence").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > cInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        cWater?.each 	{ d ->
        def attrValue = d.latestValue("water") 
        	def stateTime = d.currentState("water").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > cInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        cVent?.each 	{ d ->
        def attrValue = d.latestValue("switch") 
        	def stateTime = d.currentState("switch").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > cInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        cLock.each 	{ d ->
        def attrValue = d.latestValue("lock") 
        	def stateTime = d.currentState("lock").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > cInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }
        cSwitch.each 	{ d ->
        	def attrValue = d.latestValue("switch") 
            if (d?.currentState("switch") != null) {
            def stateTime = d?.currentState("switch").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > cInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
           	}
        }        
        cLock.each 	{ d ->
        def attrValue = d.latestValue("lock") 
        	def stateTime = d.currentState("lock").date.time
			def endTime = now() + location.timeZone.rawOffset
    		def startTimeAdj = new Date(stateTime + location.timeZone.rawOffset)
    			startTimeAdj = startTimeAdj.getTime()
    		int hours = (int)((endTime - startTimeAdj) / (1000 * 60 * 60) )
    		//int minutes = (int)((endTime - startTime) / ( 60 * 1000))
                if ( hours > cInactiveDev ) {
                    DeviceDetails << d.displayName 
                }
        }        
        def dUniqueList = DeviceDetails.unique (false)
        dUniqueList = dUniqueList.sort()       
        def listSize = dUniqueList.size()
        def listDev = dUniqueList
        result = [listSize: listSize, listDev: listDev]
        return result //dUniqueListString
	}
    	} catch (Throwable t) {
        log.error t
        result = "Oh no, something went wrong. If this happens again, please reach out for help!"
        state.pTryAgain = true
        return result
	}
}
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
		if (debug) log.debug "PIN response pending - '${state.pinTry}'"  
        return result
	}        
    if (pin == cPIN || command == cPIN || pinNum == cPIN || unit == cPIN ) {
		def data = state.savedPINdata != null ? state.savedPINdata : lastAction
        state.pTryAgain = false
        //state.pinTry = null /// 2/8/2017
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
                	location.helloHome?.execute(cmd)
                	result = "I changed your location mode to " + cmd
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
            //state.pinTry = null /// 2/8/2017
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
/************************************************************************************************************
	CONTROL SUPPORT - UNIT CONVERSIONS
************************************************************************************************************/ 
private getUnitText (unit, num) {     
    def String text = (String) unit
    def String nUnit = (String) num
    if (unit == "minute" || unit == "minutes" || unit.contains ("minutes") || unit.contains ("minute")) {
    	nUnit = "minutes"
        text = num == 1 ? num + " minute" : num + " minutes" 
        return ["text": text, "unit": nUnit]
    } 
	if (unit == "degrees"  || unit.contains ("degrees")) {
		nUnit = "degrees"
        int tNum = num
        text = tNum + " degrees"
        return ["text":text, "unit":nUnit]
    }             
	if (unit == "percent" || unit.contains ("percent")) {
		nUnit = "percent"
		text = num + " percent" 
        return ["text":text, "unit":nUnit]
    }
		return ["text":text, "unit":nUnit]

}   
/***********************************************************************************************************************
    CONTROL SUPPORT - COMMANDS HANDLER
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
            if (command == "set" || command == "set level"){
                command = "setLevel"
                deviceType = "general"
            }
    		if (command == "reading" || command == "read" || command == "studying"){ 
            	command = "read" 
                deviceType = "general"
            }
			if (command == "feeling lucky" || command == "random" || command == "different colors"){ 
            	command = "random" 
                deviceType = "general"
            }
    		if (command == "cleaning" || command == "working" || command == "concentrating" || command == "concentrate" || command == "cooking"){ 
    			command = "concentrate"
                deviceType = "general"
           	}
    		if (command == "relax" || command == "relaxing" || command == "chilling" || command == "watching"){
            	command = "relax"
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
        else if (unit=="degrees" || unit.contains("degrees") ||  unit.contains("heat") ||  unit.contains("AC")) {
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
        else if  (command == "too loud" || command == "down" ) {
            command = "decrease"
            deviceType = "volume" 
        }
        else if  (command == "not loud enough" || command == "too quiet" || command == "up") {
            command = "increase"
            deviceType = "volume"
        }
    //case "Fan Control Commands":
        if  (command == "slow down" || command == "too fast" ) {
            command = "decrease"
            deviceType = "fan" 
        }
        else if  (command == "speed up" || command == "too slow") {
            command = "increase"
            deviceType = "fan" 
        }
		else if (command == "high" || command == "medium"|| command == "low") {
			deviceType = "fan"                  
		}
	//case "Other Commands":           
        if (command == "lock" || command == "unlock") {
			deviceType = "lock"                  
		}
        else if (command == "open" || command == "close") {
			deviceType = "door"                  
		}
    }
    return ["deviceType":deviceType, "command":command ]                          
}
/************************************************************************************************************
	CONTROL SUPPORT - CUSTOM CONTROL COMMANDS
************************************************************************************************************/ 
private getCustomCmd(command, unit, group, num) {
    def result
    if (command == "repeat") {
		result = getLastMessageMain()
		return result
    }
	if (command == "change" || command == "changed" || command == "replace" || command == "replaced") {
		if (unit=="filters") {
        result = scheduleHandler(unit)
      	}
		return result
    } 
    if (command == "cancel" || command == "stop" || command == "disable" || command == "deactivate" || command == "unschedule" || command == "disarm") {
    	if (unit == "reminder" || unit == "reminders" || unit == "timer" || unit == "timers" || unit.contains ("reminder") || unit.contains ("timer") || unit.contains ("schedule") ) {
        	if (unit.contains ("reminder") || unit.contains ("schedule")) {
            	if (state.scheduledHandler != null) {
                	if (state.scheduledHandler == "filters") {
                    	unschedule(filtersHandler)
                        state.scheduledHandler = null
		                result = "Ok, canceling reminder to replace HVAC filters"
                    }
                    else {
                    state.pTryAgain = true
                    result = "Sorry, I couldn't find any scheduled reminders"// for " + state.scheduledHandler
                    }
                    return result
            	}
				else {
                	state.pTryAgain = true
					result = "Sorry, I couldn't find any scheduled reminders"// for " + state.scheduledHandler
				}
				return result
            }
            else {
                if (unit.contains ("timer") || unit.contains ("delay")) {
                    unschedule(controlHandler)
                    unschedule(securityHandler)
                    result = "Ok, canceling timer"
                    return result
                }
            }
        }
		if (unit == "conversation" || unit.contains ("conversation")) {
			state.pContCmds = false
            result = "Ok, disabling conversational features. To activate just say, start the conversation"
			return result
        }
		if (unit == "pin number" || unit == "pin") {
			if (state.usePIN_T == true || state.usePIN_D == true || state.usePIN_L == true || state.usePIN_S == true || state.usePIN_SHM == true || state.usePIN_Mode == true) {
			state.lastAction = "disable" + group
			command = "validation"
			num = 0
			def secUnit = group
			def process = false
				if (state.usePIN_T == true && group == "thermostats") 		{process = true}
				else if (state.usePIN_L == true && group == "locks") 		{process = true}
                else if (state.usePIN_D == true && group == "doors") 		{process = true}
                else if (state.usePIN_S == true && group == "switches") 	{process = true}                              
                else if (state.usePIN_SHM == true && group == "security") 	{process = true} 
                else if (state.usePIN_Mode == true && group == "modes") 	{process = true} 
				if(process == true) {
                		result = pinHandler(pin, command, num, secUnit)
                		return result
                    }
                    else {
                    	result = "The pin number for " + group + " is not active"
                        return result
                    }
            }
            else{
            	result = "The pin number for " + group + " is not enabled"
				return result
			}         
		}
        if (unit == "feedback") {
        	state.pMuteAlexa = true
            result = "Ok, disabling Alexa feedback. To activate just say, activate the feedback"
            return result
		}
		if (unit == "short answer" || unit == "short answers") {
        	state.pShort = false
            result = "Ok, disabling short answers. To activate just say, enable the short answers"
            return result
		}        
        if (unit == "undefined" && group == "undefined" ) {
        	state.pContCmdsR = "clear" 
            result = "Ok, I am here when you need me "
            return result
		}        
    }
	if (command == "start" || command == "enable" || command == "activate" || command == "schedule" || command == "arm") {
		if (unit == "reminder" || unit == "reminders" || unit == "timer" || unit == "timers" || unit.contains ("reminder") || unit.contains ("timer") ) {
        	state.scheduledHandler = "reminder"
            result = "Ok, reminder scheduled"
           	return result
    	}
		if (unit == "conversation" || unit.contains ("conversation")) {
           state.pContCmds = true        
           result = "Ok, activating conversational features. To disable just say, stop the conversation"
            return result
        }
        if (unit == "feedback") {
        	state.pMuteAlexa = false
            result = "Ok, activating Alexa feedback. To disable just say, stop the feedback"
            return result
		}
		if (unit == "short answer" || unit == "short answers") {
        	state.pShort = true
            result = "Ok, short answers on"
            return result
		}
        if (unit == "pin number" || unit == "pin") {		
			if (group == "thermostats" || group == "locks" || group == "doors" || group == "switches" || group == "security" ) {
				if (group == "thermostats") {state.usePIN_T 	= true}
                else if (group == "locks") 		{state.usePIN_L 	= true}
                else if (group == "doors") 		{state.usePIN_D 	= true}
                else if (group == "switches") 	{state.usePIN_S 	= true}                              
                else if (group == "security") 	{state.usePIN_SHM 	= true} 
                else if (group == "modes") 		{state.usePIN_Mode 	= true} 
                	state.pTryAgain = false
                    result = "Ok, the pin has been activated for " + group + ".  To disable, just say disable the PIN number for " + group
            		return result
            	}
           		else {
                	result = "Sorry, the pin number cannot be enabled for " + group
            		return result
            	}
           }      
	}
}
/******************************************************************************
	 CONTROL SUPPORT - DATE & TIME FUNCTIONS											
******************************************************************************/
private getTimeVariable(date, type) {
	def currTime
    def currDate
    def currDateShort
    def String tText = (String) null    
    def String duration = (String) null
    def today = new Date(now()).format("EEEE, MMMM d, yyyy", location.timeZone) // format("EEEE, MMMM d, yyyy") REMOVED YEAR 2/8/2017
    def yesterday = new Date(today -0.1).format("EEEE, MMMM d, yyyy", location.timeZone)
	def time = new Date(now()).format("h:mm aa", location.timeZone)
    
    currTime = new Date(date + location.timeZone.rawOffset).format("h:mm aa")                       
	currDate = new Date(date + location.timeZone.rawOffset).format("EEEE, MMMM d, yyyy")
	currDateShort = new Date(date + location.timeZone.rawOffset).format("EEEE, MMMM d")
    currDate = today == currDate ? "today" : yesterday == currDate ? "yesterday" : currDateShort
		def endTime = now() + location.timeZone.rawOffset
    	def startTime = new Date(date + location.timeZone.rawOffset)
    	startTime = startTime.getTime()
    int hours = (int)((endTime - startTime) / (1000 * 60 * 60) )
    int minutes = (int)((endTime - startTime) / ( 60 * 1000))
    duration = minutes < 60 ? minutes + " minutes" : hours + " hours"
    tText = currDate + " at " + currTime
  	return ["currTime":currTime, "currDate":currDate, "tText":tText, "duration": duration] 
 
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
    SMS HANDLER
***********************************************************************************************************************/
private void sendtxt(message) {
    if (recipients?.size()>0) { 
            sendNotificationToContacts(message, recipients)
    } 
    else {
        if (push) { 
                sendPush message
        }
        if (sms) {
            processSms(sms, message)
        }
    }
}
private void processSms(number, message) {
    if (sms) {
        def phones = sms.split("\\;")
        for (phone in phones) {
            sendSms(phone, message)
        }
    }
}
/************************************************************************************************************
   Custom Color Filter
************************************************************************************************************/       
private getColorName(cName, level) {
	if (cName == "random") {
    def randomColor = [:]
    def bulbName
    	log.warn "color is random"
        
        child?.gHues.each { bulb ->
            int hueLevel = color.l
            int hueHue = Math.random() *100 as Integer
            bulbName = bulb.name
            randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
            log.warn "setting ${bulbName} to ${randomColor}"
            //child?.gHues.setColor(hueSetVals)
            bulb.setColor(randomColor)
       	}
        log.warn "setting bulb to ${randomColor}"
        log.warn "setting ${bulbName} to ${randomColor}"
        /*
        for (bulb in child?.gHues) {    
            int hueLevel = !level ? 100 : level
            int hueHue = Math.random() *100 as Integer
            def randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
            log.warn "setting bulb to ${randomColor}"
            bulb.setColor(randomColor) 
        }
        */
        return "executed"
	}    
    for (color in fillColorSettings()) {
		if (color.name.toLowerCase() == cName.toLowerCase()) {
        	int hueVal = Math.round(color.h / 3.6)
            int hueLevel = !level ? color.l : level
			def hueSet = [hue: hueVal, saturation: color.s, level: hueLevel]
            return hueSet
		}
	}
	if (debug) log.debug "Color Match Not Found"
}
def fillColorSettings() {
	return [
		[ name: "Soft White",				rgb: "#B6DA7C",		h: 83,		s: 44,		l: 67,	],
		[ name: "Warm White",				rgb: "#DAF17E",		h: 51,		s: 20,		l: 100,	],
        [ name: "Very Warm White",			rgb: "#DAF17E",		h: 51,		s: 60,		l: 51,	],
		[ name: "Daylight White",			rgb: "#CEF4FD",		h: 191,		s: 9,		l: 99,	],
		[ name: "Cool White",				rgb: "#F3F6F7",		h: 187,		s: 19,		l: 100,	],
		[ name: "White",					rgb: "#FFFFFF",		h: 0,		s: 0,		l: 100,	],
		[ name: "Alice Blue",				rgb: "#F0F8FF",		h: 208,		s: 100,		l: 97,	],
		[ name: "Antique White",			rgb: "#FAEBD7",		h: 34,		s: 78,		l: 91,	],
		[ name: "Aqua",						rgb: "#00FFFF",		h: 180,		s: 100,		l: 50,	],
		[ name: "Aquamarine",				rgb: "#7FFFD4",		h: 160,		s: 100,		l: 75,	],
		[ name: "Azure",					rgb: "#F0FFFF",		h: 180,		s: 100,		l: 97,	],
		[ name: "Beige",					rgb: "#F5F5DC",		h: 60,		s: 56,		l: 91,	],
		[ name: "Bisque",					rgb: "#FFE4C4",		h: 33,		s: 100,		l: 88,	],
		[ name: "Blanched Almond",			rgb: "#FFEBCD",		h: 36,		s: 100,		l: 90,	],
		[ name: "Blue",						rgb: "#0000FF",		h: 240,		s: 100,		l: 50,	],
		[ name: "Blue Violet",				rgb: "#8A2BE2",		h: 271,		s: 76,		l: 53,	],
		[ name: "Brown",					rgb: "#A52A2A",		h: 0,		s: 59,		l: 41,	],
		[ name: "Burly Wood",				rgb: "#DEB887",		h: 34,		s: 57,		l: 70,	],
		[ name: "Cadet Blue",				rgb: "#5F9EA0",		h: 182,		s: 25,		l: 50,	],
		[ name: "Chartreuse",				rgb: "#7FFF00",		h: 90,		s: 100,		l: 50,	],
		[ name: "Chocolate",				rgb: "#D2691E",		h: 25,		s: 75,		l: 47,	],
		[ name: "Coral",					rgb: "#FF7F50",		h: 16,		s: 100,		l: 66,	],
		[ name: "Corn Flower Blue",			rgb: "#6495ED",		h: 219,		s: 79,		l: 66,	],
		[ name: "Corn Silk",				rgb: "#FFF8DC",		h: 48,		s: 100,		l: 93,	],
		[ name: "Crimson",					rgb: "#DC143C",		h: 348,		s: 83,		l: 58,	],
		[ name: "Cyan",						rgb: "#00FFFF",		h: 180,		s: 100,		l: 50,	],
		[ name: "Dark Blue",				rgb: "#00008B",		h: 240,		s: 100,		l: 27,	],
		[ name: "Dark Cyan",				rgb: "#008B8B",		h: 180,		s: 100,		l: 27,	],
		[ name: "Dark Golden Rod",			rgb: "#B8860B",		h: 43,		s: 89,		l: 38,	],
		[ name: "Dark Gray",				rgb: "#A9A9A9",		h: 0,		s: 0,		l: 66,	],
		[ name: "Dark Green",				rgb: "#006400",		h: 120,		s: 100,		l: 20,	],
		[ name: "Dark Khaki",				rgb: "#BDB76B",		h: 56,		s: 38,		l: 58,	],
		[ name: "Dark Magenta",				rgb: "#8B008B",		h: 300,		s: 100,		l: 27,	],
		[ name: "Dark Olive Green",			rgb: "#556B2F",		h: 82,		s: 39,		l: 30,	],
		[ name: "Dark Orange",				rgb: "#FF8C00",		h: 33,		s: 100,		l: 50,	],
		[ name: "Dark Orchid",				rgb: "#9932CC",		h: 280,		s: 61,		l: 50,	],
		[ name: "Dark Red",					rgb: "#8B0000",		h: 0,		s: 100,		l: 27,	],
		[ name: "Dark Salmon",				rgb: "#E9967A",		h: 15,		s: 72,		l: 70,	],
		[ name: "Dark Sea Green",			rgb: "#8FBC8F",		h: 120,		s: 25,		l: 65,	],
		[ name: "Dark Slate Blue",			rgb: "#483D8B",		h: 248,		s: 39,		l: 39,	],
		[ name: "Dark Slate Gray",			rgb: "#2F4F4F",		h: 180,		s: 25,		l: 25,	],
		[ name: "Dark Turquoise",			rgb: "#00CED1",		h: 181,		s: 100,		l: 41,	],
		[ name: "Dark Violet",				rgb: "#9400D3",		h: 282,		s: 100,		l: 41,	],
		[ name: "Deep Pink",				rgb: "#FF1493",		h: 328,		s: 100,		l: 54,	],
		[ name: "Deep Sky Blue",			rgb: "#00BFFF",		h: 195,		s: 100,		l: 50,	],
		[ name: "Dim Gray",					rgb: "#696969",		h: 0,		s: 0,		l: 41,	],
		[ name: "Dodger Blue",				rgb: "#1E90FF",		h: 210,		s: 100,		l: 56,	],
		[ name: "Fire Brick",				rgb: "#B22222",		h: 0,		s: 68,		l: 42,	],
		[ name: "Floral White",				rgb: "#FFFAF0",		h: 40,		s: 100,		l: 97,	],
		[ name: "Forest Green",				rgb: "#228B22",		h: 120,		s: 61,		l: 34,	],
		[ name: "Fuchsia",					rgb: "#FF00FF",		h: 300,		s: 100,		l: 50,	],
		[ name: "Gainsboro",				rgb: "#DCDCDC",		h: 0,		s: 0,		l: 86,	],
		[ name: "Ghost White",				rgb: "#F8F8FF",		h: 240,		s: 100,		l: 99,	],
		[ name: "Gold",						rgb: "#FFD700",		h: 51,		s: 100,		l: 50,	],
		[ name: "Golden Rod",				rgb: "#DAA520",		h: 43,		s: 74,		l: 49,	],
		[ name: "Gray",						rgb: "#808080",		h: 0,		s: 0,		l: 50,	],
		[ name: "Green",					rgb: "#008000",		h: 120,		s: 100,		l: 25,	],
		[ name: "Green Yellow",				rgb: "#ADFF2F",		h: 84,		s: 100,		l: 59,	],
		[ name: "Honeydew",					rgb: "#F0FFF0",		h: 120,		s: 100,		l: 97,	],
		[ name: "Hot Pink",					rgb: "#FF69B4",		h: 330,		s: 100,		l: 71,	],
		[ name: "Indian Red",				rgb: "#CD5C5C",		h: 0,		s: 53,		l: 58,	],
		[ name: "Indigo",					rgb: "#4B0082",		h: 275,		s: 100,		l: 25,	],
		[ name: "Ivory",					rgb: "#FFFFF0",		h: 60,		s: 100,		l: 97,	],
		[ name: "Khaki",					rgb: "#F0E68C",		h: 54,		s: 77,		l: 75,	],
		[ name: "Lavender",					rgb: "#E6E6FA",		h: 240,		s: 67,		l: 94,	],
		[ name: "Lavender Blush",			rgb: "#FFF0F5",		h: 340,		s: 100,		l: 97,	],
		[ name: "Lawn Green",				rgb: "#7CFC00",		h: 90,		s: 100,		l: 49,	],
		[ name: "Lemon Chiffon",			rgb: "#FFFACD",		h: 54,		s: 100,		l: 90,	],
		[ name: "Light Blue",				rgb: "#ADD8E6",		h: 195,		s: 53,		l: 79,	],
		[ name: "Light Coral",				rgb: "#F08080",		h: 0,		s: 79,		l: 72,	],
		[ name: "Light Cyan",				rgb: "#E0FFFF",		h: 180,		s: 100,		l: 94,	],
		[ name: "Light Golden Rod Yellow",	rgb: "#FAFAD2",		h: 60,		s: 80,		l: 90,	],
		[ name: "Light Gray",				rgb: "#D3D3D3",		h: 0,		s: 0,		l: 83,	],
		[ name: "Light Green",				rgb: "#90EE90",		h: 120,		s: 73,		l: 75,	],
		[ name: "Light Pink",				rgb: "#FFB6C1",		h: 351,		s: 100,		l: 86,	],
		[ name: "Light Salmon",				rgb: "#FFA07A",		h: 17,		s: 100,		l: 74,	],
		[ name: "Light Sea Green",			rgb: "#20B2AA",		h: 177,		s: 70,		l: 41,	],
		[ name: "Light Sky Blue",			rgb: "#87CEFA",		h: 203,		s: 92,		l: 75,	],
		[ name: "Light Slate Gray",			rgb: "#778899",		h: 210,		s: 14,		l: 53,	],
		[ name: "Light Steel Blue",			rgb: "#B0C4DE",		h: 214,		s: 41,		l: 78,	],
		[ name: "Light Yellow",				rgb: "#FFFFE0",		h: 60,		s: 100,		l: 94,	],
		[ name: "Lime",						rgb: "#00FF00",		h: 120,		s: 100,		l: 50,	],
		[ name: "Lime Green",				rgb: "#32CD32",		h: 120,		s: 61,		l: 50,	],
		[ name: "Linen",					rgb: "#FAF0E6",		h: 30,		s: 67,		l: 94,	],
		[ name: "Maroon",					rgb: "#800000",		h: 0,		s: 100,		l: 25,	],
		[ name: "Medium Aquamarine",		rgb: "#66CDAA",		h: 160,		s: 51,		l: 60,	],
		[ name: "Medium Blue",				rgb: "#0000CD",		h: 240,		s: 100,		l: 40,	],
		[ name: "Medium Orchid",			rgb: "#BA55D3",		h: 288,		s: 59,		l: 58,	],
		[ name: "Medium Purple",			rgb: "#9370DB",		h: 260,		s: 60,		l: 65,	],
		[ name: "Medium Sea Green",			rgb: "#3CB371",		h: 147,		s: 50,		l: 47,	],
		[ name: "Medium Slate Blue",		rgb: "#7B68EE",		h: 249,		s: 80,		l: 67,	],
		[ name: "Medium Spring Green",		rgb: "#00FA9A",		h: 157,		s: 100,		l: 49,	],
		[ name: "Medium Turquoise",			rgb: "#48D1CC",		h: 178,		s: 60,		l: 55,	],
		[ name: "Medium Violet Red",		rgb: "#C71585",		h: 322,		s: 81,		l: 43,	],
		[ name: "Midnight Blue",			rgb: "#191970",		h: 240,		s: 64,		l: 27,	],
		[ name: "Mint Cream",				rgb: "#F5FFFA",		h: 150,		s: 100,		l: 98,	],
		[ name: "Misty Rose",				rgb: "#FFE4E1",		h: 6,		s: 100,		l: 94,	],
		[ name: "Moccasin",					rgb: "#FFE4B5",		h: 38,		s: 100,		l: 85,	],
		[ name: "Navajo White",				rgb: "#FFDEAD",		h: 36,		s: 100,		l: 84,	],
		[ name: "Navy",						rgb: "#000080",		h: 240,		s: 100,		l: 25,	],
		[ name: "Old Lace",					rgb: "#FDF5E6",		h: 39,		s: 85,		l: 95,	],
		[ name: "Olive",					rgb: "#808000",		h: 60,		s: 100,		l: 25,	],
		[ name: "Olive Drab",				rgb: "#6B8E23",		h: 80,		s: 60,		l: 35,	],
		[ name: "Orange",					rgb: "#FFA500",		h: 39,		s: 100,		l: 50,	],
		[ name: "Orange Red",				rgb: "#FF4500",		h: 16,		s: 100,		l: 50,	],
		[ name: "Orchid",					rgb: "#DA70D6",		h: 302,		s: 59,		l: 65,	],
		[ name: "Pale Golden Rod",			rgb: "#EEE8AA",		h: 55,		s: 67,		l: 80,	],
		[ name: "Pale Green",				rgb: "#98FB98",		h: 120,		s: 93,		l: 79,	],
		[ name: "Pale Turquoise",			rgb: "#AFEEEE",		h: 180,		s: 65,		l: 81,	],
		[ name: "Pale Violet Red",			rgb: "#DB7093",		h: 340,		s: 60,		l: 65,	],
		[ name: "Papaya Whip",				rgb: "#FFEFD5",		h: 37,		s: 100,		l: 92,	],
		[ name: "Peach Puff",				rgb: "#FFDAB9",		h: 28,		s: 100,		l: 86,	],
		[ name: "Peru",						rgb: "#CD853F",		h: 30,		s: 59,		l: 53,	],
		[ name: "Pink",						rgb: "#FFC0CB",		h: 350,		s: 100,		l: 88,	],
		[ name: "Plum",						rgb: "#DDA0DD",		h: 300,		s: 47,		l: 75,	],
		[ name: "Powder Blue",				rgb: "#B0E0E6",		h: 187,		s: 52,		l: 80,	],
		[ name: "Purple",					rgb: "#800080",		h: 300,		s: 100,		l: 25,	],
		[ name: "Red",						rgb: "#FF0000",		h: 0,		s: 100,		l: 50,	],
		[ name: "Rosy Brown",				rgb: "#BC8F8F",		h: 0,		s: 25,		l: 65,	],
		[ name: "Royal Blue",				rgb: "#4169E1",		h: 225,		s: 73,		l: 57,	],
		[ name: "Saddle Brown",				rgb: "#8B4513",		h: 25,		s: 76,		l: 31,	],
		[ name: "Salmon",					rgb: "#FA8072",		h: 6,		s: 93,		l: 71,	],
		[ name: "Sandy Brown",				rgb: "#F4A460",		h: 28,		s: 87,		l: 67,	],
		[ name: "Sea Green",				rgb: "#2E8B57",		h: 146,		s: 50,		l: 36,	],
		[ name: "Sea Shell",				rgb: "#FFF5EE",		h: 25,		s: 100,		l: 97,	],
		[ name: "Sienna",					rgb: "#A0522D",		h: 19,		s: 56,		l: 40,	],
		[ name: "Silver",					rgb: "#C0C0C0",		h: 0,		s: 0,		l: 75,	],
		[ name: "Sky Blue",					rgb: "#87CEEB",		h: 197,		s: 71,		l: 73,	],
		[ name: "Slate Blue",				rgb: "#6A5ACD",		h: 248,		s: 53,		l: 58,	],
		[ name: "Slate Gray",				rgb: "#708090",		h: 210,		s: 13,		l: 50,	],
		[ name: "Snow",						rgb: "#FFFAFA",		h: 0,		s: 100,		l: 99,	],
		[ name: "Spring Green",				rgb: "#00FF7F",		h: 150,		s: 100,		l: 50,	],
		[ name: "Steel Blue",				rgb: "#4682B4",		h: 207,		s: 44,		l: 49,	],
		[ name: "Tan",						rgb: "#D2B48C",		h: 34,		s: 44,		l: 69,	],
		[ name: "Teal",						rgb: "#008080",		h: 180,		s: 100,		l: 25,	],
		[ name: "Thistle",					rgb: "#D8BFD8",		h: 300,		s: 24,		l: 80,	],
		[ name: "Tomato",					rgb: "#FF6347",		h: 9,		s: 100,		l: 64,	],
		[ name: "Turquoise",				rgb: "#40E0D0",		h: 174,		s: 72,		l: 56,	],
		[ name: "Violet",					rgb: "#EE82EE",		h: 300,		s: 76,		l: 72,	],
		[ name: "Wheat",					rgb: "#F5DEB3",		h: 39,		s: 77,		l: 83,	],
		[ name: "White Smoke",				rgb: "#F5F5F5",		h: 0,		s: 0,		l: 96,	],
		[ name: "Yellow",					rgb: "#FFFF00",		h: 60,		s: 100,		l: 50,	],
		[ name: "Yellow Green",				rgb: "#9ACD32",		h: 80,		s: 61,		l: 50,	],
	]
}
/***********************************************************************************************************************
    MISC. - LAST MESSAGE HANDLER
***********************************************************************************************************************/
private getLastMessageMain() {
	def outputTxt = "The last message sent was," + state.lastMessage + ", and it was sent to, " + state.lastIntent + ", at, " + state.lastTime
    return  outputTxt 
}
/***********************************************************************************************************************
 		WEATHER FEATURES
 ***********************************************************************************************************************/
def private mGetWeather(){
	state.pTryAgain = false
    def result ="Today's weather is not available at the moment, please try again later"
	try {
    	def weather = getWeatherFeature("forecast", settings.wZipCode)
        
        if(settings.wImperial){
			result = "Today's forecast is " + weather.forecast.txt_forecast.forecastday[0].fcttext  + " Tonight it will be " + weather.forecast.txt_forecast.forecastday[1].fcttext 
        }
        else {
    		result = "Today's forecast is " + weather.forecast.txt_forecast.forecastday[0].fcttext_metric + " Tonight it will be " + weather.forecast.txt_forecast.forecastday[1].fcttext_metric
	   	}
		
        return result
	}
	catch (Throwable t) {
		log.error t
        state.pTryAgain = true
        return result
	}
}
def private mGetWeatherTemps(){
	state.pTryAgain = false
    def result ="Today's temperatures not available at the moment, please try again later"
        try {
            def weather = getWeatherFeature("forecast", settings.wZipCode)
            def tHigh = weather.forecast.simpleforecast.forecastday[0].high.fahrenheit.toInteger()
            def tLow = weather.forecast.simpleforecast.forecastday[0].low.fahrenheit.toInteger()
            if(settings.wImperial){
                result = "Today's low temperature is: " + tLow  + ", with a high of " + tHigh
                return result
            }
            else {
                def tHighC = weather.forecast.simpleforecast.forecastday[0].high.celsius.toInteger()
                def tLowC = weather.forecast.simpleforecast.forecastday[0].low.celsius.toInteger()
                result = "Today's low temperature is: " + tLowC  + ", with a high of " + tHighC
                return result
        	}
        }
        catch (Throwable t) {
            log.error t
            state.pTryAgain = true
            return result
        }
	}
def private mGetWeatherAlerts(){
	def result ="There are no weather alerts for your area"
        try {
            def weather = getWeatherFeature("alerts", settings.wZipCode)
            def alert = weather.alerts.description.toString()
            def expire = weather.alerts.expires
            def DT = weather.alerts.expires_epoch
            if(alert){
                result = alert  + " is in effect for your area, that expires at " + expire
            }
            else { result }
        
        }
        catch (Throwable t) {
            log.error t
            return result
        }
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
private def textVersion() {
	def text = "4.0"
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
/***********************************************************************************************************************
 		UI - SKILL DETAILS
 ***********************************************************************************************************************/
private getControlDetails() {
	def sec = [] 
	def modes = location.modes.name.sort()
        modes?.each { m -> 
		sec +=m +"\n" } 
	def routines = location.helloHome?.getPhrases()*.label.sort()
    	routines?.each { p -> 
			sec +=p +"\n" } 	
	def security = ["off", "away", "stay", "staying", "leaving","status"]
    	security?.each { s -> 
			sec +=s +"\n" }     
    	
        def dUniqueList = sec.unique (false)
        dUniqueList = dUniqueList.sort()
        def dUniqueListString = dUniqueList.join("")
        return dUniqueListString
}
private getDeviceDetails() {
	def DeviceDetails = [] 
        //switches
        cSwitch.each 	{DeviceDetails << it.displayName +"\n"}
        cTstat.each 	{DeviceDetails << it.displayName +"\n"}
        cLock.each 		{DeviceDetails << it.displayName +"\n"}     
        cMotion.each 	{DeviceDetails << it.displayName +"\n"}
        cContact.each 	{DeviceDetails << it.displayName +"\n"}
        cPresence.each 	{DeviceDetails << it.displayName +"\n"}
        cDoor.each 		{DeviceDetails << it.displayName +"\n"}
        cWater.each 	{DeviceDetails << it.displayName +"\n"}
        cSpeaker.each 	{DeviceDetails << it.displayName +"\n"}
        cVent.each 		{DeviceDetails << it.displayName +"\n"}
        cFan.each 		{DeviceDetails << it.displayName +"\n"}
		cVent.each		{DeviceDetails << it.displayName +"\n"}
    	cRelay.each		{DeviceDetails << it.displayName +"\n"}
        cSynth.each		{DeviceDetails << it.displayName +"\n"}
        cMedia.each		{DeviceDetails << it.displayName +"\n"} 
        cBattery.each	{DeviceDetails << it.displayName +"\n"}
        cMiscDev.each	{DeviceDetails << it.displayName +"\n"} // added 2/1/2017 BD
        if(cMedia) {
            cMedia.each {a ->         
                def activities = a.currentState("activities").value
                def activityList = new groovy.json.JsonSlurper().parseText(activities)
                    activityList.each { it ->  
                    	def activity = it
                            DeviceDetails << activity.name +"\n"
                    }
            }
        }   
        def dUniqueList = DeviceDetails.unique(false)
        dUniqueList = dUniqueList.sort()
        def dUniqueListString = dUniqueList.join("")
        return dUniqueListString
}
/************************************************************************************************************
   Page status and descriptions 
************************************************************************************************************/       
//	Naming Conventions: 
// 	description = pageName + D (E.G: description: mIntentD())
// 	state = pageName + S (E.G: state: mIntentS(),
/************************************************************************************************************/       
/** Main Profiles Page **/
def mIntentS(){
	def result = ""
    def IntentS = ""
    if (cSwitch || cFan || cDoor || cRelay || cTstat || cIndoor || cOutDoor || cVent || cMotion || cContact || cWater || cPresence || cSpeaker || cSynth || cMedia || cBattery) {
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
    if (childApps.size()) {
    	result = "complete"	
    }
    result
}
def mRoomsD() {
    def text = "No Profiles have been configured. Tap here to begin"
    def ch = childApps.size()     
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
    def text = "There are no modules installed"
    if (notifyOn || securityOn) { 
            text = "Modules are Installed"
    }
    text
}
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
    def text = "The Dashboard is not Configured"
    if (mLocalWeather || mWeatherConfig || ThermoStat1 || ThermoStat2 || tempSens1 || tempSens2 || tempSens3 || tempSens4 || tempSens5) { 
            text = "Tap here to view the Dashboard"
    }
    text
}
/** Main Intent Page **/
def mDevicesS() {def result = ""
    if (cSwitch || cFan || cDoor || cRelay || cTstat || cIndoor || cOutDoor || cVent || cMotion || cContact || cWater || cPresence || cSpeaker || cSynth || cMedia || cBattery) {
    	result = "complete"}
   		result}
def mDevicesD() {def text = "Tap here to configure settings" 
    if (cSwitch || cFan || cDoor || cRelay || cTstat || cIndoor || cOutDoor || cVent || cMotion || cContact || cWater || cPresence || cSpeaker || cSynth || cMedia || cBattery) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}  
def mDefaultsS() {def result = ""
    if (cLevel || cVolLevel || cTemperature || cHigh || cMedium || cLow || cFanLevel || cLowBattery || cInactiveDev || cFilterReplacement || cFilterSynthDevice || cFilterSonosDevice) {
    	result = "complete"}
   		result}
def mDefaultsD() {def text = "Tap here to configure settings" 
    if (cLevel || cVolLevel || cTemperature || cHigh || cMedium || cLow || cFanLevel || cLowBattery || cInactiveDev || cFilterReplacement || cFilterSynthDevice || cFilterSonosDevice) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}         
def mSecurityS() {def result = ""
    if (cMiscDev || cRoutines || uPIN_SHM || uPIN_Mode || fSecFeed || shmSynthDevice || shmSonosDevice || volume || resumePlaying) {
    	result = "complete"}
   		result}
def mSecurityD() {def text = "Tap here to configure settings" 
    if (cMiscDev || cRoutines || uPIN_SHM || uPIN_Mode || fSecFeed || shmSynthDevice || shmSonosDevice || volume || resumePlaying) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}
        
      

