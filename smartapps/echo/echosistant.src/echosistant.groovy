/*
 * EchoSistant - The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enabled Device.
 *
 *		02/04/2017		Release 3.1.6	Added Timestamp to SMS, Push, and notify for profiles and notification alerts		
 *		02/03/2017		Release 3.1.5	Bug Fix - Fixed ability to run more than one Routine in a profile
 *		01/04/2017		Release 3.1.4	Bug Fix - Notifications for Motion and Switches not working. Fixed.
 *		01/01/2017		Release 3.1.3	Bug Fix - Door locks not working, now they do.
 *		01/01/2017		Release 3.1.2	Bug Fix - Devices not showing in List_of_Devices in Logs
 *		01/01/2017		Release 3.1.1	Ceiling Fan Controls - speed up, slow down, low, medium, high
 *		12/31/2016		Release 3.1.0	Lock Control - commands are lock and unlock
 *		12/31/2016		Release 3.0.9	Garage Door control - commands are open and close
 *		12/29/2016		Release 3.0.8	Bug Fix to correct error for auto adjustment of light levels
 *		12/25/2016		Release 3.0.7	Profile triggered mode change, Home Status page, canned Alexa Message
 *		12/23/2016		Release 3.0.6	Version 3.0 Release Version
 *		12/22/2016		Release 3.0.5 	Alert variables (Bobby) 
 *		12/19/2016		Release 3.0.4 	Final Review of 3.0 version (Bobby)
 *		12/14/2016		Release 3.0.4 	Bug Fixes and Changes by Jason - Garage alert not playing.
 *		12/09/2016		Release 3.0.2	Major overhaul of UI and process (cotnt'd)
 *		12/09/2016		Release 3.0.1	Major overhaul of UI and process (in progress)
 *		??/??/2016		Release 3.0.0	Additions: Msg to Notify Tab in Mobile App, Push Msg, Complete Reconfigure of Profile Build, More Control of Dimmers, and Switches,
 *										Device Activity Alerts Page, Toggle control, Flash control for switches. 
 *										Bug fixes: Time out error resolved.
 *		11/04/2016      Version 1.0		Initial Release
 *
 * ROADMAP
 * Alarms and Timers with Voice Feedback
 * External TTS
 * External SMS
 * Google Calendar
 * Personal Message Que per User
 *
 *
 *
 *  Copyright 2016 Jason Headley
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
	name			: "EchoSistant${parent ? " - Profile" : ""}",
    namespace		: "Echo",
    author			: "JH",
	description		: "The Ultimate Voice Controlled Assistant Using Alexa Enabled Devices.",
	singleInstance	: true,
    parent: parent ? "Echo.EchoSistant" : null,
    category		: "My Apps",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/

/************************************************************************************************************
   PARENT PAGES
************************************************************************************************************/
preferences {
    page name: "pageMain"
    //Parent Pages    
    page name: "mainParentPage"
    	page name: "support"
    	page name: "profiles"
    	page name: "about"
        page name: "Alerts"
        page name: "Choices"
        page name: "Integrations"
    	page name: "CoRE"
        page name: "skillDetails"
        page name: "ProfileDetails"
        page name: "DeviceDetails"
		page name: "devicesControlMain"
		page name: "tokens"
    	page name: "pageConfirmation"
    	page name: "pageReset"
        page name: "AlexaFeelings"
        page name: "homeStatus"
        page name: "homeStatusConfig"


    //Profile Pages    
    page name: "mainProfilePage"
    	page name: "MsgPro"
    	page name: "SMS"
    	page name: "DevPro"
    	page name: "devicesControl"    
   		page name: "StaPro"
        page name: "FeedBack"
		page name: "MsgConfig"
    	page name: "certainTime"   
}
def pageMain() { if (!parent) mainParentPage() else mainProfilePage() }
/***********************************************************************************************************************
    PARENT UI CONFIGURATION
***********************************************************************************************************************/
page name: "mainParentPage"
    def mainParentPage() {	
       dynamicPage(name: "mainParentPage", title:"", install: true, uninstall:false) {
            //go to Parent set up
            section ("") {
                href "profiles", title: "Profiles", description: profilesDescr(), state: completeProfiles(),
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"    
                href "about", title: "Notifications, Device Controls, and Integrations", description: settingsDescr(), state: completeSettings(),
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"                
                href "homeStatus", title: "What is the status of my Home?", 
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"                
				href "support", title: "EchoSistant Security and Support", description: supportDescrL(), state: completeProfiles(),
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Amazon_alexa.png"                               
                }
            section ("") {
                paragraph "${textCopyright()}"
			}
		}
    }
page name: "profiles"
    def profiles() {
            dynamicPage (name: "profiles", title: "", install: false, uninstall: false) {
            if (childApps.size()) { 
                    section(childApps.size()==1 ? "One Profile configured" : childApps.size() + " Profiles configured" )
            }
            section("Create New Profiles"){
                    app(appName: "EchoSistant", namespace: "Echo", multiple: true, description: "Tap Here to Create a New Profile...")
            } 
        } 
    }
page name: "support"    
    def support(){
        dynamicPage(name: "support", title: "View Support Information",install: false, uninstall: true) {
            section ("Developers", hideWhenEmpty: true){  
            	paragraph ("You can reach out to the Echosistant Developers with the following information: \n" + 
                "Jason Headley \n"+
                "Forum user name @bamarayne \n" +
                "Bobby Dobrescu \n"+
                "Forum user name @SBDobrescu")
                }
                section("Debugging") {
                    input "debug", "bool", title: "Enable Debug Logging", default: false, submitOnChange: true 
                    }
                section ("Apache License"){
                    input "ShowLicense", "bool", title: "Show License", default: false, submitOnChange: true
                    def msg = textLicense()
                        if (ShowLicense) paragraph "${msg}"
                    }
                section ("Security Tokens"){
                    paragraph ("Log into the IDE on your computer and navigate to the Live Logs tab. Leave that window open, come back here, and open this section")
                    input "ShowTokens", "bool", title: "Show Security Tokens", default: false, submitOnChange: true
                    if (ShowTokens) paragraph "The Security Tokens are now displayed in the Live Logs section of the IDE"
                    def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. OAuth may not be enabled. "+
                    "Go to the SmartApp IDE settings to enable OAuth."	
                    if (ShowTokens) log.info "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                    if (ShowTokens) paragraph 	"Access token:\n"+
                                                "${msg}\n"+
                                                "Application ID:\n"+
                                                "${app.id}"
                    }
                section ("Revoke/Renew Access Token & Application ID"){
                    href "tokens", title: "Revoke/Reset Security Access Token", description: none
                    }
            	section ("Directions, How-to's, and Troubleshooting") { 
 					href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant", title: "Tap to go to the EchoSistant Wiki", description: none,
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png"
                	}
 				section ("Amazon AWS Skill Details") { 
					href "SkillDetails", title: "Tap to view setup data for the AWS Main Intent Skill...", description: "",
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/echosistant_About.png"
            		}                
            	section ("AWS Lambda website") {
            		href url:"https://aws.amazon.com/lambda/", title: "Tap to go to the AWS Lambda Website", description: none,
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_aws.png"
                	}
            	section ("Amazon Developer website") {    
   					href url:"https://developer.amazon.com/", title: "Tap to go to Amazon Developer website", description: none,
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Skills.png"
            	}
                section("Tap below to remove the ${textAppName()} application.  This will remove ALL Profiles and the App from the SmartThings mobile App."){
                	}
				}             
			}
page name: "about"
 def about(){
        dynamicPage(name: "about", uninstall: false) {
                section("Device and Action Notifications") {
	                href "Alerts", title: "Create and View Notifications...",description: AlertProDescr() , state: completeAlertPro(),
    	        	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Rest.png"
				}
                section ("'Alexa Feelings' and Device Control"){
                	href "Integrations", title: "Device Control Settings", description: ParConDescr() , state: completeParCon(), 
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png" 
                         }
                section ("CoRE Integration") { 
					href "CoRE", title: "Tap to read about CoRe Integration..." , description: "" , state: complete, 
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_CoRE.png"
            	} 
                section ("Configure the Home Status Page") {
                	href "homeStatusConfig", title: "Choose devices to display on the Home Status Page", description: "" , state: comlete,               
                	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png" 
				}
			}     
		} 
page name: "homeStatus"
	def homeStatus(){
        dynamicPage(name: "homeStatus", uninstall: false) {
        section ("Current Mode") {
            paragraph "${textCurrentMode()}"
                }
        section ("ThermoStats and Temperature") {
        	def tStat1 = ThermoStat1
            def temp1 = (tStat1?.currentValue("temperature"))
            def setPC1 = (tStat1?.currentValue("coolingSetpoint"))
            def setPH1 = (tStat1?.currentValue("heatingSetpoint"))
            def mode1 = (tStat1?.currentValue("thermostatMode"))
            def tStat2 = ThermoStat2
            def temp2 = (tStat2?.currentValue("temperature"))
            def setPC2 = (tStat2?.currentValue("coolingSetpoint"))
            def setPH2 = (tStat2?.currentValue("heatingSetpoint"))
            def mode2 = (tStat2?.currentValue("thermostatMode"))
		if ("${mode1}" == "auto") 
        	paragraph "The temperature of the ${tStat1} is ${temp1} degrees. The thermostat is in ${mode1} mode, the heating setpoint is ${setPH1} and the cooling setpoint is ${setPC1}."
        if ("${mode1}" == "cool")
            paragraph "The temperature of the ${tStat1} is ${temp1} degrees, the thermostat is set to ${setPC1} degrees and is in ${mode1} mode."
        if ("${mode1}" == "heat")
            paragraph "The temperature of the ${tStat1} is ${temp1} degrees, and the thermostat is set to ${setPH1} degrees and is in ${mode1} mode."
        if ("${mode1}" == "off")
        	paragraph "The ${tStat1} theremostat is currently ${mode1}"      	
        if ("${mode2}" == "auto") 
        	paragraph "The temperature of the ${tStat2} is ${temp2} degrees. The thermostat is in ${mode2} mode, the heating setpoint is ${setPH2} and the cooling setpoint is ${setPC2}."
        if ("${mode2}" == "cool")
            paragraph "The temperature of the ${tStat2} is ${temp2} degrees, and the thermostat is set to ${setPC2} degrees and is in ${mode2} mode."
        if ("${mode2}" == "heat")
            paragraph "The temperature of the ${tStat2} is ${temp2} degrees, and the thermostat is set to ${setPH2} degrees and is in ${mode2} mode."
        if ("${mode2}" == "off")
        	paragraph "The ${tStat2} theremostat is currently ${mode2}."
		}
		section ("Temperature Sensors") {
        	def Sens1temp = (tempSens1?.currentValue("temperature"))
            def Sens2temp = (tempSens2?.currentValue("temperature"))
            def Sens3temp = (tempSens3?.currentValue("temperature"))
            def Sens4temp = (tempSens4?.currentValue("temperature"))
            def Sens5temp = (tempSens5?.currentValue("temperature"))
            if (tempSens1)
            	paragraph "The temperature of the ${tempSens1} is ${Sens1temp} degrees."
            if (tempSens2)
            	paragraph "The temperature of the ${tempSens2} is ${Sens2temp} degrees."
            if (tempSens3)
            	paragraph "The temperature of the ${tempSens3} is ${Sens3temp} degrees."
            if (tempSens4)
            	paragraph "The temperature of the ${tempSens4} is ${Sens4temp} degrees."
            if (tempSens5)
            	paragraph "The temperature of the ${tempSens5} is ${Sens5temp} degrees."
			}
		} 
	}      
        
        
page name: "homeStatusConfig"
	def homeStatusConfig(){
        dynamicPage(name: "homeStatusConfig", uninstall: false) {
        section ("Thermoststats") {
        	input "ThermoStat1", "capability.temperatureMeasurement", title: "First ThermoStat", required: false, default: false, submitOnChange: true 
        	input "ThermoStat2", "capability.temperatureMeasurement", title: "Second ThermoStat", required: false, default: false, submitOnChange: true 
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
page name: "Choices"    
    def Choices(){
        dynamicPage(name: "Choices", title: "Choose from the available Notifications",install: false, uninstall: false) {
            section ("Activate/DeActivate Notifications", hideWhenEmpty: true){
            input "timeStamp", "bool", title: "Add time stamp to Text and Push Messages", required: false, defaultValue: false
            paragraph "To mute a notification, disable its toggle \n" +
            "To mute all notifications, disable the Top toggle"
            input "allNotifications", "bool", title: "Turn on to Activate the Notifications Section", default: false, submitOnChange: true
            input "switchesAndDimmers", "bool", title: "Switches and Dimmers", default: false, submitOnChange: true
            input "doorsAndWindows", "bool", title: "Doors and Windows", default: false, submitOnChange: true
            input "Locks", "bool", title: "Locks", default: false, submitOnChange: true
            input "Motion", "bool", title: "Motion Sensors", default: false, submitOnChange: true
            input "Presence", "bool", title: "Presence Sensors", default: false, submitOnChange: true
            input "TStats", "bool", title: "Thermostats", default: false, submitOnChange: true
            //input "Water", "bool", title: "Water Sensors", default: false, submitOnChange: true
		}
    }
}            

page name: "Alerts"
    def Alerts(){
        dynamicPage(name: "Alerts", uninstall: false) {
        section ("Activate and Deactivate Notifications"){
            href "Choices", title: "Activate and Deactivate Notifications", description: none,
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Rest.png"
        	}
        if (!allNotifications) {
        section("") {
        paragraph ("All Notifications have been disabled.  Activate 'All Notifications' to configure this section")
        	}
        }
        if (allNotifications) {
        section ("Switches and Dimmers", hideWhenEmpty: true) {
            if (TheSwitch || audioTextOn || audioTextOff || speech1 || push1 || notify1 || music1) paragraph "Configured with Settings"
            if (switchesAndDimmers) {
            input "ShowSwitches", "bool", title: "Switches and Dimmers", default: false, submitOnChange: true
            if (ShowSwitches) {        
                input "TheSwitch", "capability.switch", title: "Choose Switches...", required: false, multiple: true, submitOnChange: true
				input "audioTextOn", "audioTextOn", title: "Play this message", description: "...when the switch turns on", required: false, capitalization: "sentences"
                input "audioTextOff", "audioTextOff", title: "Play this message", description: "...when the switch turns off", required: false, capitalization: "sentences"
                input "speech1", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
                input "music1", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music1) {
                    input "volume1", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying1", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg1", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg1) {
                	input "push1", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify1", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
            	}
            }             
        }
	}        
        section("Doors and Windows", hideWhenEmpty: true) {
            if (TheContact || audioTextOpen || audioTextClosed || speech2 || push2 || notify2 || music2) paragraph "Configured with Settings"
            if (doorsAndWindows) {
            input "ShowContacts", "bool", title: "Doors and Windows", default: false, multiple: false, submitOnChange: true
            if (ShowContacts) {
                input "TheContact", "capability.contactSensor", title: "Choose Doors and Windows..", required: false, multiple: true, submitOnChange: true
                input "audioTextOpen", "textOpen", title: "Play this message", description: "Message to play when the door opens", required: false, capitalization: "sentences"
                input "audioTextClosed", "textClosed", title: "Play this message", description: "Message to play when the door closes", required: false, capitalization: "sentences"
                input "speech2", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
            	input "music2", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music2) {
                    input "volume2", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying2", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg2", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg2) {
                	input "push2", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify2", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
            	}
            }
        }
	}      
        section("Locks", hideWhenEmpty: true) {
            if (TheLock || audioTextLocked || audioTextUnlocked || speech3 || push3 || notify3 || music3) paragraph "Configured with Settings"
            if (Locks) {
            input "ShowLocks", "bool", title: "Locks", default: false, submitOnChange: true
            if (ShowLocks) {
                input "TheLock", "capability.lock", title: "Choose Locks...", required: false, multiple: true, submitOnChange: true
                input "audioTextLocked", "textLocked", title: "Play this message", description: "Message to play when the lock locks", required: false, capitalization: "sentences"
                input "audioTextUnlocked", "textUnlocked", title: "Play this message", description: "Message to play when the lock unlocks", required: false, capitalization: "sentences"
                input "speech3", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
            	input "music3", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music3) {
                    input "volume3", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying3", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg3", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg3) {
                	input "push3", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify3", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
                }
            }
        }
	}        
        section("Motion Sensors", hideWhenEmpty: true) {
            if (TheMotion || audioTextActive || audioTextInactive || speech4 || push4 || notify4 || music4) paragraph "Configured with Settings"
            if (Motion) {
            input "ShowMotion", "bool", title: "Motion Sensors", default: false,  submitOnChange: true
            if (ShowMotion) {
                input "TheMotion", "capability.motionSensor", title: "Choose Motion Sensors...", required: false, multiple: true, submitOnChange: true
                input "audioTextActive", "textActive", title: "Play this message", description: "Message to play when motion is detected", required: false, capitalization: "sentences"
                input "audioTextInactive", "textInactive", title: "Play this message", description: "Message to play when motion stops", required: false, capitalization: "sentences"
                input "speech4", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
            	input "music4", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music4) {
                    input "volume4", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying4", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg4", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg4) {
                	input "push4", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify4", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
                }
            }
        }
	}        
        section("Presence Sensors", hideWhenEmpty: true) {
        	if (ThePresence || audioTextPresent || audioTextNotPresent || speech5 || push5 || notify5 || music5) paragraph "Configured with Settings"
            if (Presence) {
            input "ShowPresence", "bool", title: "Presence Sensors", default: false, submitOnChange: true
            if (ShowPresence) {
                input "ThePresence", "capability.presenceSensor", title: "Choose Presence Sensors...", required: false, multiple: true, submitOnChange: true
                input "audioTextPresent", "textPresent", title: "Play this message", description: "Message to play when the Sensor arrives", required: false, capitalization: "sentences"
                input "audioTextNotPresent", "textNotPresent", title: "Play this message", description: "Message to play when the Sensor Departs", required: false, capitalization: "sentences"
                input "speech5", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
                input "music5", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music5) {
                    input "volume5", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying5", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg5", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg5) {
                	input "push5", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify5", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
            	}
			}
		}
	}         
         section("Thermostats", hideWhenEmpty: true) {
        	if (TheThermostat || audioTextHeating || audioTextCooling || speech8 || push8 || notify8 || music8) paragraph "Configured with Settings"
            if (TStats) {
            input "ShowTstat", "bool", title: "Thermostats", default: false, submitOnChange: true
            if (ShowTstat) {
                input "TheThermostat", "capability.thermostat", title: "Choose Thermostats...", required: false, multiple: true, submitOnChange: true
                input "audioTextHeating", "textHeating", title: "Play this message", description: "Message to play when the Heating Set Point Changes", required: false, capitalization: "sentences"
                input "audioTextCooling", "textCooling", title: "Play this message", description: "Message to play when the Cooling Set Point Changes", required: false, capitalization: "sentences" 
                input "speech8", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
                input "music8", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music8) {
                    input "volume8", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying8", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg8", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg8) {
                	input "push8", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify8", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
                        }
                    }
                }		
            } 
        }
    }
}    
page name: "AlexaFeelings"
	def AlexaFeelings(){
        dynamicPage(name: "AlexaFeelings", title: "What are 'Alexa Feelings?'", uninstall: false) {
        	section("What are 'Alexa Feelings'?"){
            paragraph "Alexa Feelings offers users a better way of controlling certain lights or group of lights. You may have tried to tell Alexa "+ 
            "that it is too bright in the living room, and watched Alexa making your lights, well...brighter. With EchoSistant, if you say to "+ 
            "the light or group of lights it is too bright, it will actually dim your lights. You can also say, it is not dark enough or simply "+ 
            "say, make it darker, and your lights will dim the way you want them to.\n"+ 
			" \n" +
			"You may have tried to tell Alexa to dim your group of lights and watched Alexa dimming only the first light on the list. "+
            "With EchoSistants ability to dim the group ensures that each dimmer in the group is adjusted according to individual light levels./n" +
			" \n" +
			"EchoSistant gives users a quick way of controlling multiple media devices in one Room. Have the TV on, a connected speaker on, and other " +
            "media devices making noise in one area when an important call comes through? Just tell Echosistant to mute the first floor* and " +
            "the noise stops so you can pick up the call.\n" +
            " \n" +
            "*Assumes you have named your devices appropriately\n" +
            " \n" +
            "Do you have a connected Thermostat?  Ever tried to tell Alexa to adjust the temperature only to be told that your device can not " +
            "be found in your account?  Well, now you just tell Alexa that it's too cold or that it's too hot, and your thermostat will take the " +
            "appropriate actions to ensure you're comfortable."            
            	}
            }
		}            
page name: "Integrations"
	def Integrations(){
    		dynamicPage(name: "Integrations", title: " 'Alexa Feelings' and Device Control", uninstall: false){
				section ("") {            
                href "AlexaFeelings", title: "Tap to learn about Alexa Feelings", description: "", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/echosistant_About.png"
                href "devicesControlMain", title: "Control These Devices with Voice by speaking commands to Alexa (via the Main Skill)", description: ParConDescr() , state: completeParCon(),
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"            			
                    paragraph ("Define Variables for Voice Controlled Devices (for increase/decrease commands)")
                    input "cLevel", "number", title: "Alexa Automatically Adjusts Light Levels by using a scale of 1-10 (default is +/-3)", defaultValue: 3, required: false
                    input "fLevel", "number", title: "Alexa Automatically Adjusts Ceiling Fans by this percentage (default is 33%) when using commands 'speed up & slow down'", defaultValue: 33, required: false
                    input "cTemperature", "number", title: "Alexa Automatically Adjusts temperature by using a scale of 1-10 (default is +/-1)", defaultValue: 1, required: false
//                    input "cPIN", "number", title: "Set a PIN number to prevent unathorized use of Voice Control", default: false, required: false
					}
                }
			}    

page name: "CoRE"
    def CoRE(){
            dynamicPage(name: "CoRE", uninstall: false) {
                section ("Welcome to the CoRE integration page"){
                    paragraph ("This integration is in place to enhance the"+
                    "communication abilities of EchoSistant and your SmartThings Home Automation Project, allowing you more control and flexibility.\n"+
                    "CoRE integration is currently one way only. You can NOT trigger profiles from within CoRE."+
                    "CoRE listens for a profile execution and then performs the programmed tasks.\n"+
                    "Configuration is simple. In EchoSistant create your profile. Then open CoRE and create a new piston. In the condition section choose 'EchoSistant Profile'"+
                    "as the trigger. Choose the appropriate profile and then finish configuring the piston.\n"+
                    "When the profile is executed the CoRE piston will also execute.")
                    href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant#CoRE_Integration", title: "Tap here for more information", description: none
                 }   
            }
        } 
                
page name: "SkillDetails"
    def SkillDetails(){
            dynamicPage(name: "SkillDetails", uninstall: false) {
 			section ("List of Profiles") { 
				href "ProfileDetails", title: "View your List of Profiles for copy & paste to the AWS Skill...", description: "", state: "complete", 
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/echosistant_About.png"
            }
            section ("List of Devices") {
				href "DeviceDetails", title: "View your List of Devices for copy & paste to the AWS Skill...", description: "", state: "complete", 
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/echosistant_About.png"            	
				}
					
				   
            }
        }         
page name: "ProfileDetails"
    def ProfileDetails(){
            dynamicPage(name: "ProfileDetails", uninstall: false) {
 			section ("List of Profiles") { 
                def ProfileList = getProfileDetails()   
                    paragraph ("${ProfileList}")
                      	log.info "${ProfileList} "
                        }
					}
				} 
page name: "DeviceDetails"
    def DeviceDetails(){
            dynamicPage(name: "DeviceDetails", uninstall: false) {
 			section ("List of Devices") { 
                def DeviceList = getDeviceDetails()   
                    paragraph ("${DeviceList}")
                      	log.info "${DeviceList} "
                        }
					}
                }    
page name: "devicesControlMain"    
    def devicesControlMain(){
        dynamicPage(name: "devicesControlMain", title: "Select Devices That Alexa Can Control Directly",install: false, uninstall: false) {
        	
            section ("Switches", hideWhenEmpty: true){
                input "cSwitches", "capability.switch", title: "Control These Switches...", multiple: true, required: false, submitOnChange: true
            }
            section ("Dimmers", hideWhenEmpty: true){
                input "cDimmers", "capability.switchLevel", title: "Control These Dimmers...", multiple: true, required: false, submitOnChange: true
            }
            section ("Doors and Locks", hideWhenEmpty: true){
            	input "cGdoor", "capability.garageDoorControl", title: "Allow These Garage Door(s)...", multiple: true, required: false, submitOnChange: true
            	input "cGlock", "capability.lock", title: "Allow These Lock(s)...", multiple: true, required: false, submitOnChange: true
			}
            section ("Thermostats", hideWhenEmpty: true){
                input "cTstat", "capability.thermostat", title: "Control These Thermostat(s)...", multiple: true, required: false
            }
//            section ("", hideWhenEmpty: true){
//                input "showAdvanced", "bool", title: "Advanced Options", default: false, submitOnChange: true
//				if (showAdvanced) {	
//                    href "devicesControlCustom", title: "Create custom devices and commands", description: devicesControlCustomDescr() , state: completeDevicesControlCustom(),
//                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Plus.png"                    
//            	}
//            }
        }
    }
    
page name: "devicesControlCustom"    
    def devicesControlCustom(){
        dynamicPage(name: "devicesControlCustom", title: "Create Devices That Alexa Can Control Directly",install: false, uninstall: false) {
            section ("Create a Device", hideWhenEmpty: true){
                input "custSwitch1", "capability.switch", title: "Select Device...", multiple: false, required: false, submitOnChange: true
                input "custName1", "text", title: "Name Device...", multiple: false, required: false
				if(custSwitch1) {                           
                        def availableCommands = custSwitch1.capabilities                      
                    	availableCommands.sort()
                        if (debug) log.debug "availableCommands = $availableCommands"
                        paragraph "Add any of these commands to your LIST_OF_COMMANDS custom slot: $availableCommands"
    			}
            }
            if (custSwitch1) {
                section ("+ Create another Device", hideWhenEmpty: true){
                    input "custSwitch2", "capability.switch", title: "Select Device...", multiple: false, required: false, submitOnChange: true
                    input "custName2", "text", title: "Name Device...", multiple: false, required: false
                    if(custSwitch2) {
						def availableCommands = custSwitch2.supportedCommands                       
                    	availableCommands.sort()
                        if (debug) log.debug "availableCommands = $availableCommands"
                        paragraph "Add any of these commands to your LIST_OF_COMMANDS custom slot: $availableCommands"
    				} 
                }
            }
            if (custSwitch2) {
                section ("+ Create another Device", hideWhenEmpty: true){
                    input "custSwitch3", "capability.switch", title: "Select Device...", multiple: false, required: false, submitOnChange: true
                    input "custName3", "text", title: "Name Device...", multiple: false, required: false
                    if(custSwitch3) {
						def availableCommands = custSwitch2.supportedCommands                       
                    	availableCommands.sort()
                        if (debug) log.debug "availableCommands = $availableCommands"
                        paragraph "Add any of these commands to your LIST_OF_COMMANDS custom slot: $availableCommands"
    				}
                }
            }
            if (custSwitch3) {
                section ("+ Create another Device", hideWhenEmpty: true){
                    input "custSwitch4", "capability.switch", title: "Select Device...", multiple: false, required: false, submitOnChange: true
                    input "custName4", "text", title: "Name Device...", multiple: false, required: false
                    if(custSwitch4) {
						def availableCommands = custSwitch2.supportedCommands                       
                    	availableCommands.sort()
                        if (debug) log.debug "availableCommands = $availableCommands"
                        paragraph "Add any of these commands to your LIST_OF_COMMANDS custom slot: $availableCommands"
    				}
                }
            }
            if (custSwitch4) {            
                section ("+ Create another Device", hideWhenEmpty: true){
                    input "custSwitch5", "capability.switch", title: "Select Device...", multiple: false, required: false, submitOnChange: true
                    input "custName5", "text", title: "Name Device...", multiple: false, required: false
                    if(custSwitch5) {
						def availableCommands = custSwitch2.supportedCommands                       
                    	availableCommands.sort()
                        if (debug) log.debug "availableCommands = $availableCommands"
                        paragraph "Add any of these commands to your LIST_OF_COMMANDS custom slot: $availableCommands"
    				}
                }
            }
        }
    }    

page name: "tokens"
    def tokens(){
            dynamicPage(name: "tokens", title: "Security Tokens", uninstall: false){
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
                        log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                section ("Reset Access Token / Application ID"){
                    href "pageConfirmation", title: "Reset Access Token and Application ID", description: none
                    }
                }
            } 
page name: "pageConfirmation"
    def pageConfirmation(){
            dynamicPage(name: "pageConfirmation", title: "Reset/Renew Access Token Confirmation", uninstall: false){
                section {
                    href "pageReset", title: "Reset/Renew Access Token", description: "Tap here to confirm action - READ WARNING BELOW"
                    paragraph "PLEASE CONFIRM! By resetting the access token you will disable the ability to interface this SmartApp with your Amazon Echo."+
                    "You will need to copy the new access token to your Amazon Lambda code to re-enable access." +
                    "Tap below to go back to the main menu with out resetting the token. You may also tap Done above."
                    }
                section(" "){
                    href "mainParentPage", title: "Cancel And Go Back To Main Menu", description: none 
                    }
                }
            }
page name: "pageReset"
    def pageReset(){
            dynamicPage(name: "pageReset", title: "Access Token Reset", uninstall: false){
                section{
                    revokeAccessToken()
                    state.accessToken = null
                    OAuthToken()
                    def msg = state.accessToken != null ? "New access token:\n${state.accessToken}\n\n" : "Could not reset Access Token."+
                    "OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
                    paragraph "${msg}"
                    paragraph "The new access token and app ID are now displayed in the Live Logs tab of the IDE."
                    log.info "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                }
                section(" "){ 
                    href "mainParentPage", title: "Tap Here To Go Back To Main Menu", description: none 
                    }
                }
            } 
/***********************************************************************************************************************
    PROFILE UI CONFIGURATION
***********************************************************************************************************************/
def mainProfilePage() {	
    dynamicPage(name: "mainProfilePage", title:"I Want This Profile To...", install: true, uninstall: true) {
        section {
           	href "MsgPro", title: "Send Audio Messages to these devices...", description: MsgProDescr(), state: completeMsgPro(),
   				image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png" 
            href "SMS", title: "Send Text & Push Messages...", description: SMSDescr() , state: completeSMS(),
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
  			href "DevPro", title: "Execute Actions when Profile runs...", description: DevProDescr(), state: completeDevPro(),
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"            			
  			href "groups", title: "Create Profile Device Groups...", description: groupsDescr(), state: completeGroups(),
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Plus.png" 
			href "MsgConfig", title: "With These Global Message Options...", description: MsgConfigDescr() , state: completeMsgConfig(),
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png" 
            }
        section ("Name and/or Remove this Profile") {
 		   	label title:"              Rename Profile ", required:false, defaultValue: "New Profile"  
        }    
	}
}
page name: "MsgPro"
    def MsgPro(){
        dynamicPage(name: "MsgPro", title: "Using These Media Devices...", uninstall: false){
             section ("Music Player", hideWhenEmpty: true){
                input "sonosDevice", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true,
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
                if (sonosDevice) {
                        def availableCommands = sonosDevice.capabilities                      
                    	availableCommands.sort()
                        if (parent.debug) log.info "availableCommands = $availableCommands"

                    input "volume", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                }
            }  
            section ("Speech Synthesis", hideWhenEmpty: true) {
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
                input "synthDevice", "capability.speechSynthesis", title: "On this Speech Synthesis Type Devices", multiple: true, 
                required: false, submitOnChange: true,
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
            }        
        }   
    }
page name: "SMS"
    def SMS(){
        dynamicPage(name: "SMS", title: "Send SMS and/or Push Messages...", uninstall: false) {
            section ("Text Messages" ) {
            input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
                if (sendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
           	input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true,          
                        image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
                if (sendText){      
                    paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122;8046663344"
                    input name: "sms", title: "Send text notification to (optional):", type: "phone", required: false
//                	input "timeStamp", "bool", title: "Add time stamp to Text and Push Messages", required: false, defaultValue: false
                }
            }    
            section ("Push Messages") {
            input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
            input "notify", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png"
            }        
        }        
    }
page name: "DevPro"	 
    def DevPro(){
        dynamicPage(name: "DevPro", uninstall: false) {
            section ("Trigger these lights and/or execute these routines when the Profile runs...") {
                href "devicesControl", title: "Select switches...", description: DevConDescr() , state: completeDevCon(),
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"
                input "newMode", "enum", title: "Choose Mode to change to...", options: location.modes.name.sort(), multiple: false, required: false, submitOnChange: true, 
					image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
				def actions = location.helloHome?.getPhrases()*.label 
                if (actions) {
                    actions.sort()
                    log.trace actions
            	input "runRoutine", "enum", title: "Select a Routine to execute", required: false, options: actions, multiple: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
                if (runRoutine) {
                input "runRoutine2", "enum", title: "Select a Second Routine to execute", required: false, options: actions, multiple: false,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
            		}
                }
            }
        }
    }
page name: "devicesControl"
    def devicesControl() {
            dynamicPage(name: "devicesControl", title: "Select Devices to use with this profile",install: false, uninstall: false) {
                 section ("Light Switches", hideWhenEmpty: true){
                    input "switches", "capability.switch", title: "Select Lights and Switches...", multiple: true, required: false, submitOnChange: true
                        if (switches) input "switchCmd", "enum", title: "What do you want to do with these switches?", options:["on":"Turn on","off":"Turn off","toggle":"Toggle","delay":"Delay"], multiple: false, required: false, submitOnChange:true
                        if (switchCmd) {
                        input "sSecondsOn", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                        input "sSecondsOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false
                        }
                        if (switchCmd) input "otherSwitch", "capability.switch", title: "...and these other switches?", multiple: true, required: false, submitOnChange: true
                        if (otherSwitch) input "otherSwitchCmd", "enum", title: "What do you want to do with these other switches?", options: ["on1":"Turn on","off1":"Turn off","toggle1":"Toggle","delay1":"Delay"], multiple: false, required: false, submitOnChange: true
                        if (otherSwitchCmd) {
                        input "sSecondsOn1", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                        input "sSecondsOff1", "number", title: "Turn off in Seconds?", defaultValue: none, required: false
                    	}
                    }
        		section ("Colored lights", hideWhenEmpty: true){
            		input "hues", "capability.colorControl", title: "Select These Colored Lights...", multiple: true, required: false, submitOnChange:true
            			if (hues) {
                        	input "hueCmd", "enum", title: "What do you want to do with these color bulbs?", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false, submitOnChange:true
							if ("setColor") input "color", "enum", title: "Hue Color?", required: false, multiple:false, options: parent.fillColorSettings().name
							input "lightLevel", "enum", title: "Light Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]]                       
        					}
						}
                section ("Dimmers", hideWhenEmpty: true){
                    input "dimmers", "capability.switchLevel", title: "Select Dimmers...", multiple: true, required: false , submitOnChange:true
                        if (dimmers) input "dimmersCmd", "enum", title: "Command To Send To Dimmers", options:["set":"Set level","delay2":"Delay","off":"Turn off"], multiple: false, required: false, submitOnChange:true
                        if (dimmersCmd) input "dimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false, submitOnChange: true
                            if (dimmersCmd) {
                        		input "sSecondsOn2", "number", title: "Turn on in Seconds?", defaultValue: none, required: false, submitOnChange: true
                        		input "sSecondsOff2", "number", title: "Turn off in Seconds?", defaultValue: none, required: false, submitOnChange: true
                        		}	
                        if (dimmersLVL) input "otherDimmers", "capability.switchLevel", title: "Control These Other Dimmers...", multiple: true, required: false , submitOnChange:true
                        if (otherDimmers) input "otherDimmersCmd", "enum", title: "Command To Send To Other Dimmers", options:["set":"Set level","delay3":"Delay","off":"Turn off"], multiple: false, required: false, submitOnChange:true
                        if (otherDimmersCmd) input "otherDimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false, submitOnChange: true
                			if (otherDimmersCmd) {
                        		input "sSecondsOn3", "number", title: "Turn on in Seconds?", defaultValue: none, required: false, submitOnChange: true
                        		input "sSecondsOff3", "number", title: "Turn off in Seconds?", defaultValue: none, required: false, submitOnChange: true
                        		}
                    		}
                section ("Flash These Switches") {
					input "flashSwitches", "capability.switch", title: "Select Flashers", multiple: true, required: false, submitOnChange:true
					if (flashSwitches) {
					input "Fcolor", "enum", title: "Flasher Color?", required: false, multiple:false, options: ["Red","Green","Blue","Yellow","Orange","Purple","Pink"]
 					input "FlashLevel", "enum", title: "Light Flash Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]]                       
                    input "numFlashes", "number", title: "This number of times (default 3)", required: false, submitOnChange:true
                    input "onFor", "number", title: "On for (default 1 second)", required: false, submitOnChange:true
					input "offFor", "number", title: "Off for (default 1 second)", required: false, submitOnChange:true
					}
                }
			}
		}
page name: "groups"
    def groups() {
    	dynamicPage(name: "groups", title: "Select Devices to create groups that can be controlled by Alexa within this Profile",install: false, uninstall: false) {
        	section ("Group of Lights ", hideWhenEmpty: true){
                    input "gSwitches", "capability.switch", title: "Select Lights and Switches...", multiple: true, required: false, submitOnChange: true
                    if (gSwitches) {
                    	paragraph "You can now control this group by speaking commands to Alexa:  \n" +
                        " E.G: Alexa tell Main Skill, to turn on/off the lights in the Profile Name"
                    }
                    input "cVent", "capability.switchLevel", title: "Select these Vents...", multiple: true, required: false, submitOnChange: true
                }
            }
      	}
	
page name: "MsgConfig"
    def MsgConfig(){
        dynamicPage(name: "MsgConfig", title: "Configure Global Profile Options...", uninstall: false) {
            section ("Voice Message Options and Alexa Responses") {
            input "MsgOpt", "bool", title: "I want to use these configuration options...", defaultValue: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
                if (MsgOpt) {
                	input "AprofileMsg", "bool", title: "Have Alexa speak a predetermined message when profile executes?", default: false, submitOnChange: true 
                		if (AprofileMsg) input "AprofileMsgTxt", "Text", title: "Play this message when this profile executes", description: none, required: false, capitalization: "sentences"
					input "ShowPreMsg", "bool", title: "Pre-Message (plays on Audio Playback Device before message)", defaultValue: false, submitOnChange: true
                        if (ShowPreMsg) input "PreMsg", "text", title: "Pre-Message...", defaultValue: none, submitOnChange: true, required: false 
                    input "Acustom", "bool", title: "Custom Response from Alexa...", defaultValue: false, submitOnChange: true
                        if (Acustom) input "outputTxt", "text", title: "Input custom phrase...", required: false, defaultValue: "Message sent,   ", submitOnChange: true
                    input "Arepeat", "bool", title: "Alexa repeats message to sender when sent...", defaultValue: false, submitOnChange: true
                        if (Arepeat) {			
                        if (Arepeat && Acustom){
                            paragraph "NOTE: only one custom Alexa response can"+
                            " be delivered at once. Please only enable Custom Response OR Repeat Message"
                            }				
                        }
                    input "AfeedBack", "bool", title: "Turn on to disable Alexa Feedback Responses (silence Alexa) Overrides all other Alexa Options...", defaultValue: false, submitOnChange: true
                    input "disableTts", "bool", title: "Disable All spoken notifications (No voice output from the speakers or Alexa)", required: false, submitOnChange: true  
                    input "ContCmds", "bool", title: "Allow Alexa to prompt for additional commands after message is delivered...", defaultValue: false, submitOnChange: true
                    input "ContCmdsR", "bool", title: "Allow Alexa to prompt for additional commands after Repeat command is given...", defaultValue: false, submitOnChange: true
                    }
                }            	
            section ("Mode Restrictions") {
                input "modes", "mode", title: "Only when mode is", multiple: true, required: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
                }        
            section ("Days - Audio only on these days"){	
                input "runDay", title: "Only on certain days of the week", multiple: true, required: false, submitOnChange: true,
                    "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
                }
            section ("Time - Audio only during these times"){
                href "certainTime", title: "Only during a certain time", description: timeIntervalLabel ?: "Tap to set", state: timeIntervalLabel ? "complete" : null,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
                }   
	        }
    	}
page name: "certainTime"
    def certainTime() {
        dynamicPage(name:"certainTime",title: "Only during a certain time", uninstall: false) {
            section("Beginning at....") {
                input "startingX", "enum", title: "Starting at...", options: ["A specific time", "Sunrise", "Sunset"], required: false , submitOnChange: true
                if(startingX in [null, "A specific time"]) input "starting", "time", title: "Start time", required: false, submitOnChange: true
                else {
                    if(startingX == "Sunrise") input "startSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    else if(startingX == "Sunset") input "startSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                }
            }
            section("Ending at....") {
                input "endingX", "enum", title: "Ending at...", options: ["A specific time", "Sunrise", "Sunset"], required: false, submitOnChange: true
                if(endingX in [null, "A specific time"]) input "ending", "time", title: "End time", required: false, submitOnChange: true
                else {
                    if(endingX == "Sunrise") input "endSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    else if(endingX == "Sunset") input "endSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                }
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
//************************************************************************************************************
mappings {
	path("/b") { action: [GET: "processBegin"] }
	path("/c") { action: [GET: "controlDevices"] }
    path("/t") {action: [GET: "processTts"]}
}
/************************************************************************************************************
		Base Process
************************************************************************************************************/
def installed() {
	if (debug) log.debug "Installed with settings: ${settings}"
	initialize()
}
def updated() { 
	if (debug) log.debug "Updated with settings: ${settings}"
    unsubscribe()
    initialize()
}

def initialize() {
	if (!parent){
    	if (debug) log.debug "Initializing Parent app"
        sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
    	state.lastMessage = null
		state.lastIntent  = null
    	state.lastTime  = null
        state.lambdaReleaseTxt = "Not Set"
        state.lambdaReleaseDt = "Not Set" 
		state.lambdatextVersion = "Not Set"
    	def children = getChildApps()
    		if (debug) log.debug "$children.size Profiles installed"
			children.each { child ->
			}
			if (!state.accessToken) {
        		if (debug) log.debug "Access token not defined. Attempting to refresh. Ensure OAuth is enabled in the SmartThings IDE."
                OAuthToken()
        		if (debug) log.debug "STappID = '${app.id}' , STtoken = '${state.accessToken}'" 
			}
        subscribeToEvents()
	}
	else{
        unschedule()
    }
    if (parent) {
        if (parent.debug) log.debug "Initializing Child app"
        state.lastMessage = null
    	state.lastTime  = null
        state.recording = null
        subscribeChildToEvents()
     }
     else{
        unschedule()
	}
}
/************************************************************************************************************
		Subscriptions
************************************************************************************************************/
def subscribeChildToEvents() {
    	if (debug) log.debug "Subscribing Child apps to events"
	if (runModes) {
		subscribe(runMode, location.currentMode, modeChangeHandler)
	}
    if (runDay) {
   		subscribe(runDay, location.day, location.currentDay)
		} 
    }

def subscribeToEvents() {
	if (allNotifications) {
    	if (debug) log.debug "Subscribing Parent app to events"
    	if (switchesAndDimmers) {
            if (TheSwitch) {
                if (audioTextOn) {subscribe(TheSwitch, "switch.on", alertsHandler)}
                if (audioTextOff) {subscribe(TheSwitch, "switch.off", alertsHandler)}
                }    
			}
        if (doorsAndWindows) {
            if (TheContact) {
                if (audioTextOpen) {subscribe(TheContact, "contact.open", alertsHandler)}
                if (audioTextClosed) {subscribe(TheContact, "contact.closed", alertsHandler)}
                }
        }
        if (Locks) {
            if (TheLock) {
                if (audioTextLocked) {subscribe(TheLock, "lock.locked", alertsHandler)}
                if (audioTextUnlocked) {subscribe(TheLock, "lock.unlocked", alertsHandler)}
                }
        }
        if (Motion) {
            if (TheMotion) {
                if (audioTextActive) {subscribe(TheMotion, "motion.active", alertsHandler)}
                if (audioTextInactive) {subscribe(TheMotion, "motion.inactive", alertsHandler)}
                }
    	}
        if (Presence) {
            if (ThePresence) {
                if (audioTextPresent || audioTextNotPresent ) {subscribe(ThePresence, "presence", alertsHandler)}
                }
        }
        if (TStats) {
            if (TheThermostat) {    
                if (audioTextHeating) {subscribe(TheThermostat, "heatingSetpoint", alertsHandler)}
                if (audioTextCooling) {subscribe(TheThermostat, "coolingSetpoint", alertsHandler)}
                    }
                }
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
	def pContinue = true

    if (debug){
        log.debug "Message received from Lambda with: (ver) = '${versionTxt}', (date) = '${versionDate}', (release) = '${releaseTxt}'"+ 
        ". And sent to Lambda: pContinue = '${pContinue}', versionSTtxt = '${versionSTtxt}'"
	}
    	return ["pContinue":pContinue, "versionSTtxt":versionSTtxt]
}   
/************************************************************************************************************
   CONTROL PROCESS PROCESS (PARENT) - from Lambda via page c
************************************************************************************************************/
def controlDevices() {
        def ctCommand = params.pCommand
        def ctProfile = params.pProfile
        def ctNum = params.pNum
        def ctDevice = params.pDevice
        def ctUnit = params.pUnit
		def pintentName = params.intentName

        def outputTxt = " "
        def pContCmds = "false"
        def pContCmdsR = "false"
        def command = ctCommand
		def commandLVL = " "
        def deviceType = " "
        def numText = " "
        def result = " "        
        if (debug) log.debug "Received Lambda request to control devices with settings:" +
        						" (ctCommand)= ${ctCommand}', (ctProfile) = '${ctProfile}',"+
                                " ctNum = '${ctNum}', (ctDevice) = '${ctDevice}', (ctUnit) = '${ctUnit}', (pintentName) = '${pintentName}'"   
			    if (cLevel == null) cLevel = 3 // to fix default missing value
    //Temperature Commands
                if (command == "colder" || command =="not cold enough" || command =="too hot" || command == "too warm") {
                    commandLVL = "decrease"
                	deviceType = "cTstat"
                    if (debug) log.debug "Temperature command = '${commandLVL}'"
                }
        		if (command == "freezing" || command =="not hot enough" || command == "too chilly" || command == "too cold" || command == "warmer") {
                    commandLVL = "increase"
                    deviceType = "cTstat"
                    if (debug) log.debug "Temperature command = '${commandLVL}'"
                }
	//Dimmer Commands
                if (command == "darker" || command == "too bright" || command == "dim" || command == "dimmer" ) {
                    commandLVL = "decrease" 
                    deviceType = "cDimmers"
                    if (debug) log.debug "Light command = '${commandLVL}'"
                }
        		if (command == "not bright enough" || command == "brighter" || command == "too dark" || command == "brighten") {
                    commandLVL = "increase" 
                    deviceType = "cDimmers"
                    if (debug) log.debug "Light command = '${commandLVL}'"                   
                }
    //Ceiling Fan Commands 
				if (command == "high") {
                    commandLVL = "high" 
                    deviceType = "cDimmers"
                    if (debug) log.debug "Fan command = '${commandLVL}'"                   
                }
                if (command == "medium") {
                    commandLVL = "medium" 
                    deviceType = "cDimmers"
                    if (debug) log.debug "Fan command = '${commandLVL}'"                   
                }
                if (command == "low") {
                    commandLVL = "low" 
                    deviceType = "cDimmers"
                    if (debug) log.debug "Fan command = '${commandLVL}'"                   
                }
                if (command == "speed up") {
                    commandLVL = "speed up" 
                    deviceType = "cDimmers"
                    if (debug) log.debug "Fan command = '${commandLVL}'"                   
                }
                if (command == "slow down") {
                    commandLVL = "slow down" 
                    deviceType = "cDimmers"
                    if (debug) log.debug "Fan command = '${commandLVL}'"                   
                }
                
	//Volume Commands
                if (command == "too loud" ) {
                    commandLVL = "decrease"
                    deviceType = "cVol"
                    if (debug) log.debug "Volume command = '${commandLVL}'"
                }
        		if (command == "not loud enough") {
                    commandLVL = "increase"
                    deviceType = "cVol"
                    if (debug) log.debug "Volume command = '${commandLVL}'"
                }
                if (command == "mute" || command == "quiet" ) {
                    deviceType = "cVol"
                    if (debug) log.debug "Volume command = '${commandLVL}'"
                }
                if (command == "unmute") {
                    deviceType = "cVol"
                    if (debug) log.debug "Volume command = '${commandLVL}'"
                }

	//Global Commands
                if (command == "decrease" || command == "down") {
                    commandLVL = "decrease" 
                    deviceType = "cGlobal"
                    if (debug) log.debug "Global command = '${commandLVL}'"
                }
       			if (command == "increase" || command == "up") {
                    commandLVL = "increase"
                    deviceType = "cGlobal"
                    if (debug) log.debug "Global command = '${commandLVL}'"
                }
                if (command == "set" || command == "set level") {
                    commandLVL = "setLevel"
                    deviceType = "cGlobal"
                    if (debug) log.debug "Global command = '${commandLVL}'"
                }

                if (command == "on" || command == "off") {
                    deviceType = "cGlob"
                    if (debug) log.debug "Global command = '${command}'"
                }

//Door Commands    
			if (cGdoor.find {s -> s.label.toLowerCase() == ctDevice}) {
				if (command == "close" || command == "open") {
						def deviceMatch = cGdoor.find {s -> s.label.toLowerCase() == ctDevice} 
                            if (deviceMatch) {
                            log.debug "I Found the device: '${ctDevice}'" }
                                if (deviceMatch && command == "close") {
                                commandLVL = "close"
                                }
                                else {
                                	commandLVL = "open"
                                	}
                				deviceType = "cGdoor"
                                
                        		if (cGdoor == deviceMatch) log.debug "the cGdoor Command = '${commandLVL}'"
                					}
                                }
             if (command == "lock" || command == "unlock") {
                	deviceType = "cGlock"
                    if (debug) log.debug "Door Lock Command = '${command}'"
                }
                
           
		if (ctNum == "undefined" || ctNum =="?") {ctNum = 0}
			ctNum = ctNum as int 
      	
        if (ctUnit == "undefined" || ctUnit =="?") {ctUnit = "unknown"}
        
      	if (ctUnit == "minute" || ctUnit == "minutes") {
      		ctUnit = "MIN"
      		numText = ctNum == 1 ? ctNum + " minute" : ctNum + " minutes"                
      	}
		 else if (ctUnit == "level") {ctUnit = "LVL"}      
        else if (ctUnit == "degrees") {
            	ctUnit = "TEMP"
                numText = ctNum + " degrees"
        }
        else if (ctUnit == "percent") {
        		ctUnit = "PERC"
                numText = ctNum + " percent"    
        }
		else if (ctUnit != "TEMP" || ctUnit != "MIN") {numText = "by " + cLevel*10 + " percent"}       
        
        if (deviceType == "cTstat") {def numTxtTMP = cTemperature == 1 ? cTemperature + " degree" : cTemperature + " degrees" }

       if (ctCommand == "repeat") {
        	if (debug) log.debug "Processing repeat last message delivered to any of the Profiles"
				outputTxt = getLastMessageMain()
         	if (debug) log.debug "Received message: '${outputTxt}' ; sending to Lambda"           
       }
       if (ctCommand == "cancel") {
        	if (debug) log.debug "Canceling timmer!"
            unschedule()
			outputTxt = "Ok, canceling timer"
         	if (debug) log.debug "Cancel message received; sending '${outputTxt}' to Lambda"           
       }  
/************************************************************************************************************
   EASTER EGG HUNT STARTS
************************************************************************************************************/       
	if (ctDevice == "smart things") {
        	if (debug) log.debug "I love SmartThings!"
				outputTxt = "${loveSmartThings()}"        
       } 
/************************************************************************************************************
   EASTER EGG HUNT ENDS
************************************************************************************************************/        
       
       if (ctDevice != "undefined"){       
           if (command == "on" || command == "off") {
                 if (cSwitches) {
                        if (debug) log.debug "Searching for device type = switch, device name ='${ctDevice}'"
                        def deviceMatch = cSwitches.find {s -> s.label.toLowerCase() == ctDevice}             
                        if (deviceMatch) {
                            if (debug) log.debug "Found a device: '${deviceMatch}'"
                            if (ctNum > 0 && ctUnit == "MIN") {
                                runIn(ctNum*60, controlHandler, [data: [type: "cSwitches", command: command, device: ctDevice, unit: ctUnit, num: ctNum]])
                                outputTxt = "Ok, turning " + deviceMatch + " " + command + ", in " + numText
                            }
                            else {
                                def data = [type: "cSwitches", command: command, device: ctDevice, unit: ctUnit, num: ctNum]
                                controlHandler(data)
                                if (debug) log.debug "Processing control handler with: '${data}'"
                                outputTxt = "Ok, turning " + ctDevice + " " + command
                            }
                        }
                        else {outputTxt = "Sorry, I couldn't find a device named " + ctDevice + " in your list of selected switches"}
                  }
            }
       if (deviceType == "cGdoor"){       
           if (command == "open" || command == "close") {
                 
                        if (debug) log.debug "Searching for device type = cGdoor, device name ='${ctDevice}'"
                        def deviceMatch = cGdoor.find {s -> s.label.toLowerCase() == ctDevice }            
                        if (deviceMatch) {
                            if (debug) log.debug "Found a device: '${deviceMatch}'"
                            if (ctNum > 0 && ctUnit == "MIN") {
                                runIn(ctNum*60, controlHandler, [data: [type: "cGdoor", command: command, device: ctDevice, unit: ctUnit, num: ctNum]])
                                if (command == "open") {
                                	outputTxt = "Ok, opening the " + deviceMatch + ", in " + numText
                                	}
                                	else if (command == "close") {
                                		outputTxt = "Ok, closing the " + deviceMatch + ", in " + numText
                                	}
                             }
                             else {
                                def data = [type: "cGdoor", command: command, device: ctDevice, unit: ctUnit, num: ctNum]
                                controlHandler(data)
                                if (debug) log.debug "Processing control handler with: '${data}'"
                                if (command == "open") {
                                	outputTxt = "Ok, opening the " + deviceMatch }
                                    	else if (command == "close") {
                                    		outputTxt = "ok, closing the " + deviceMatch }
                            }
                        }
                        else {outputTxt = "Sorry, I couldn't find a device named " + ctDevice }
                  }
            } 
       if (deviceType == "cGlock"){       
           if (command == "lock" || command == "unlock") {
                 if (cGlock) {
                        if (debug) log.debug "Searching for device type = cGlock, device name ='${ctDevice}'"
                        def deviceMatch = cGlock.find {s -> s.label.toLowerCase() == ctDevice}             
                        if (deviceMatch) {
                            if (debug) log.debug "Found a device: '${deviceMatch}'"
                            if (ctNum > 0 && ctUnit == "MIN") {
                                runIn(ctNum*60, controlHandler, [data: [type: "cGlock", command: command, device: ctDevice, unit: ctUnit, num: ctNum]])
                                if (command == "lock") {
                                	outputTxt = "Ok, locking the " + deviceMatch + ", in " + numText
                                	}
                                	else if (command == "unlock") {
                                		outputTxt = "Ok, unlocking the " + deviceMatch + ", in " + numText
                                		}
                                    }
                             else {
                                def data = [type: "cGlock", command: command, device: ctDevice, unit: ctUnit, num: ctNum]
                                controlHandler(data)
                                if (debug) log.debug "Processing control handler with: '${data}'"
                                if (command == "lock") {
                                	outputTxt = "Ok, locking the " + deviceMatch }
                                    	else if (command == "unlock") {
                                    		outputTxt = "ok, unlocking the " + deviceMatch 
                                            	}
                                	else outputTxt = "Ok, setting the " + ctDevice + ", to " + command
                            }
                        }
                        else {outputTxt = "Sorry, I couldn't find a device named " + ctDevice }
							}
                        }
					}                     
            if (deviceType == "cDimmers" || deviceType == "cGlobal") {
                if (commandLVL == "decrease" || commandLVL == "increase" || commandLVL == "setLevel" || commandLVL == "speed up" || commandLVL == "slow down" || commandLVL == "high" || commandLVL == "medium" || commandLVL == "low") { 
                    if (cDimmers) {           
                        if (debug) log.debug "Searching for device type = dimmer, device name='${ctDevice}'"
                            def deviceDMatch = cDimmers.find {s -> s.label.toLowerCase() == ctDevice}
                        if (deviceDMatch) {
                            if (debug) log.debug "Found a device: '${deviceDMatch}'"
                            if (ctNum && ctUnit == "MIN") {
                                runIn(ctNum*60, delayHandler, [data: [type: "cDimmers", command: commandLVL, device: ctDevice, unit: ctUnit, num: ctNum]])                           
                                if (commandLVL == "decrease") {outputTxt = "Ok, decreasing the " + ctDevice + " level in " + numText}
                                else if (commandLVL == "increase") {outputTxt = "Ok, increasing the " + ctDevice + " level in " + numText}
                            }
                            else {
                                def data = [type: "cDimmers", command: commandLVL, device: ctDevice, unit: ctUnit, num: ctNum]
                                controlHandler(data)
                                if (debug) log.debug "Processing control handler with: '${data}'"
                                if (ctUnit == "PERC"){outputTxt = "Ok, setting " + ctDevice + " to " + numText}
                                else{
                                    if (commandLVL == "setLevel" && ctUnit == "unknown") {
                                        def num = ctNum > 10 ? ctNum : ctNum*10 as int
                                        outputTxt = "Ok, setting the " + ctDevice + " to " + num + " percent"
                                    }
                                    if (commandLVL == "decrease") {outputTxt = "Ok, decreasing the " + ctDevice + " level " + numText}
                                    if (commandLVL == "increase") {outputTxt = "Ok, increasing the " + ctDevice + " level " + numText}
                                	if (commandLVL == "speed up") {outputTxt = "Ok, increasing the " + ctDevice + " by " + fLevel + " percent "}
                                    if (commandLVL == "slow down") {outputTxt = "Ok, decreasing the " + ctDevice + " by " + fLevel + " percent "}
									if (commandLVL == "high") {outputTxt = "Ok, setting " + ctDevice + " to high "}
                                    if (commandLVL == "medium") {outputTxt = "Ok, setting " + ctDevice + " to medium "}
                                    if (commandLVL == "low") {outputTxt = "Ok, setting " + ctDevice + " to low "}
                                }
                            }
                        }
                        else {outputTxt = "Sorry, I couldn't find a device named " + ctDevice + " in your list of selected dimmers"}
                    }
                }
            }
            if (deviceType == "cTstat" || deviceType == "cGlobal") {
                if (commandLVL == "decrease" || commandLVL == "increase" || commandLVL == "setLevel" ) { 
                    if (cTstat) {           
                        if (debug) log.debug "Searching for device type= thermostat, device='${ctDevice}'"
                            def deviceDMatch = cTstat.find {s -> s.label.toLowerCase() == ctDevice}
                        if (deviceDMatch) {
                            if (ctNum && ctUnit == "MIN") {
                                runIn(ctNum*60, delayHandler, [data: [type: "cTstat", command: commandLVL, device: ctDevice, unit: ctUnit, num: ctNum]])
                                if (commandLVL == "decrease") {outputTxt = "Ok, decreasing the " + ctDevice + " temperature in " + numText}
                                else if (commandLVL == "increase") {outputTxt = "Ok, increasing the " + ctDevice + " temperature in " + numText}
                            }
                            else {
                                def data = [type: "cTstat", command: commandLVL, device: ctDevice, unit: ctUnit, num: ctNum]
                                controlHandler(data)
                                if (debug) log.debug "Processing control handler with: '${data}'"
                                if (ctUnit == "TEMP"){outputTxt = "Ok, adjusting " + ctDevice + " to " + numText}
                                else{        
                                    def numTxtTMP = cTemperature == 1 ? cTemperature + " degree" : cTemperature + " degrees"  
                                    if (commandLVL == "decrease") {outputTxt = "Ok, decreasing the temperature " + ctDevice + " by " + numTxtTMP}
                                    if (commandLVL == "increase") {outputTxt = "Ok, increasing the temperature " + ctDevice + " by " + numTxtTMP}
                                    if (commandLVL == "decrease" && ctCommand == "mute") {outputTxt = "Ok, muting " + ctDevice}
                                }
                            }
                        }
                    }
                        else {outputTxt = "Sorry, I couldn't find a device named " + ctDevice + " in your list of selected thermostats"}                
                }
            }
        }
        if (ctProfile != "undefined"){
        	def profile = childApps.find {c -> c.label.toLowerCase() == ctProfile}             
			def profileMatch = profile?.label
            if (debug) log.debug "Found a Profile match = '${profileMatch}'"
            if (profileMatch) {
            	command = commandLVL != " " ?  commandLVL : command //== "on" || command == "off" ? command : command=  
                	if (debug) log.debug "Profile new command = ${command}"
                if (command != "undefined" && command != "run") {
                    if (ctNum > 0 && ctUnit == "MIN") {
                        runIn(ctNum*60, controlHandler, [data: [type: "cProfiles", command: command, device: profileMatch, unit: ctUnit, num: ctNum]])
                        if (command == "decrease") {outputTxt = "Ok, decreasing the " + profileMatch + " lights level in " + numText}
                        else if (command == "increase") {outputTxt = "Ok, increasing the " + profileMatch + " lights level in " + numText}
                        else if (command == "on" || command == "off" ) {outputTxt = "Ok, turning " + profileMatch + " lights " + command + ", in " + numText}
                    }
                    else {
                        def data = [type: "cProfiles", command: command, device: profileMatch, unit: ctUnit, num: ctNum]
                        controlHandler(data)
                        if (debug) log.debug "Processing control handler with: '${data}'"
                        if (command == "on" || command == "on" ) {outputTxt = "Ok, turning " + profileMatch + " lights " + command}
                        if (ctUnit == "PERC"){outputTxt = "Ok, setting " + profileMatch + " lights to " + numText}
                        else{
                            if (commandLVL == "setLevel" && ctUnit == "unknown") {
                                def num = ctNum > 10 ? ctNum : ctNum*10 as int
                                outputTxt = "Ok, setting the " + profileMatch + " to " + num + " percent"
                            }
                            if (commandLVL == "decrease") {outputTxt = "Ok, decreasing the " + profileMatch + " level " + numText}
                            if (commandLVL == "increase") {outputTxt = "Ok, increasing the " + profileMatch + " level " + numText}
                            if (commandLVL == "decrease" && ctCommand == "mute") {outputTxt = "Ok, muting the noise in the" + profileMatch}
                            }
                    }
                }
                else {
                    if (command == "run") {
                    	if (debug) log.debug "Profile run command = '${ctCommand}'"
                    	
                        if (ctNum > 0 && ctUnit == "MIN") {
                                runIn(ctNum*60, controlHandler, [data: [type: "cProfiles", command: command, device: profileMatch, unit: ctUnit, num: ctNum]])
                                outputTxt = "Ok, running profile actions, for " + ctProfile + ", in " + numText
                        
                        }
                        else {
                        def data = [type: "cProfiles", command: command, device: profileMatch, unit: ctUnit, num: ctNum]
                    		controlHandler(data)                
                			outputTxt = "Ok, running profile actions, for " + ctProfile
                        }
                    }
       			}  
            }
			else {outputTxt = "Sorry, I couldn't find a profile named " + ctProfile + " in your list of selected profiles"}                        
     	}                    
        if (debug) log.debug "Sending response to Alexa with settings: '${pContCmds}' and the message:'${outputTxt}'"               
        return ["outputTxt":outputTxt, "pContCmds":pContCmds]
}
/************************************************************************************************************
   CONTROL HANDLER
************************************************************************************************************/      
def controlHandler(data) {   
    def deviceType = data.type
    def deviceCommand = data.command
   	def deviceD = data.device
    def unitU = data.unit
    def numN = data.num
	def pLevel = cLevel
    
    if (deviceType == "cProfiles") {	
		childApps.each { child ->
        	def cMatch = child.label
            if (cMatch == deviceD) {
                def pintentName = cMatch
                def ptts = "Running Profile as requested by the main intent"
                def pttx = ptts
                def pDataSet = [ptts:ptts,pttx:pttx,pintentName:pintentName] 
            	if (debug) log.debug "triggering Profile actions from Main intent with data: ${pDataSet}"
                if (deviceCommand == "on" || deviceCommand == "off") {
            		child?.gSwitches."${deviceCommand}"()
        			if (debug) log.debug "Matched Profile: ${cMatch} and processed: '${deviceCommand}' command" 
				}
				else if (deviceCommand == "increase" || deviceCommand == "decrease" || deviceCommand == "setLevel") {
                    def switchLVL
                    child?.gSwitches.each {s -> 
                    	def	currLevel = s?.latestValue("level")
                    		if (debug) log.debug "current level=  ${currLevel} for , switch ${s.label}"
                    	def currState = s?.latestValue("switch") 
                    		if (debug) log.debug "current state =  ${currState} for , switch ${s.label}"
                    	if (currLevel) {
                                def newLevel = pLevel*10
                                if (debug) log.debug "${cMatch}, ${child} with newLevel '${newLevel}'"     
                                if (unitU == "PERC") newLevel = numN 
                                    if (deviceCommand == "increase") {
                                if (unitU == "PERC") {
                                    newLevel = numN
                                }   
                                else {
                                    newLevel =  currLevel + newLevel
                                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                                }
                            }
                            if (deviceCommand == "decrease") {
                                if (unitU == "PERC") {
                                    newLevel = numN
                                }   
                                else {
                                    newLevel =  currLevel - newLevel
                                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                                }            
                            }
                            if (deviceCommand == "setLevel") {
                                if (unitU == "PERC") {
                                    newLevel = numN
                                }   
                                else {
                                    newLevel =  numN*10
                                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                                }            
                            }
                            if (newLevel > 0 && currState == "off") {
                                s.on()
                                s.setLevel(newLevel)
                            }
                            else {                                    
                                if (newLevel == 0 && currState == "on") {
                                	s.off()
                                    }
                                else {s.setLevel(newLevel)}
                            }
                        }
                        else if  (deviceCommand == "increase" && currState == "off") {s.on()}
                        else if (deviceCommand == "decrease" && currState == "on") {s.off()}
            		}
                }
                else 
                child.profileEvaluate(pDataSet)
                if (debug) log.debug "Running actions for ${cMatch}" 
        	}
    	}
    }
	if (deviceType == "cSwitches") {
			def deviceMatch = cSwitches.find {s -> s.label.toLowerCase() == deviceD}
        if (deviceMatch) {
        	deviceMatch."${deviceCommand}"()
        	if (debug) log.debug "cSwitches with command '${deviceCommand}'" 
        } 
    }
    if (deviceType == "cGdoor") {
    		def deviceMatch = cGdoor.find {s -> s.label.toLowerCase() == deviceD}
       if (deviceMatch) {
       		deviceMatch."${deviceCommand}"()
            if (debug) log.debug "cGdoor with command '${deviceCommand}'"
			}
        }   
    if (deviceType == "cGlock") {
    		def deviceMatch = cGlock.find {s -> s.label.toLowerCase() == deviceD}
       if (deviceMatch) {
       		deviceMatch."${deviceCommand}"()
            if (debug) log.debug "cGlock with command '${deviceCommand}'"
			}
    	}    
	if (deviceType == "cDimmers") {
			def deviceDMatch = cDimmers.find {s -> s.label.toLowerCase() == deviceD}
		if (deviceDMatch) {
            def currLevel = deviceDMatch.latestValue("level")
            def currState = deviceDMatch.latestValue("switch")
            def newLevel = cLevel*10 //30%
			if (unitU == "PERC") newLevel = numN      
            if (deviceCommand == "increase") {
            	if (unitU == "PERC") {
                	newLevel = numN
                }   
                else {
                	newLevel =  currLevel + newLevel
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
            	}
            }
                if (deviceCommand == "speed up") {
            	if (unitU == "PERC") {
                	newLevel = numN
                }   
                else {
                	newLevel = currLevel + fLevel 
            	}
            }
            if (deviceCommand == "high") {
            	if (unitU == "PERC") {
                	newLevel = 99
                }   
                else {
                	newLevel = 99
            	}
            }
            if (deviceCommand == "medium") {
            	if (unitU == "PERC") {
                	newLevel = 66
                }   
                else {
                	newLevel = 66
            	}
            }
            if (deviceCommand == "low") {
            	if (unitU == "PERC") {
                	newLevel = 33
                }   
                else {
                	newLevel = 33
            	}
            }
				if (deviceCommand == "decrease") {
            	if (unitU == "PERC") {
                	newLevel = numN
                }   
                else {
                	newLevel =  currLevel - newLevel
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                }            
            }
            if (deviceCommand == "slow down") {
            	if (unitU == "PERC") {
                	newLevel = numN
                }   
                else {
                	newLevel =  currLevel - fLevel
            	}            
            }
            if (deviceCommand == "setLevel") {
            	if (unitU == "PERC") {
                	newLevel = numN
                }   
                else {
                	newLevel =  numN*10
            		newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                }            
            }
			if (newLevel > 0 && currState == "off") {
            	deviceDMatch.on()
            	deviceDMatch.setLevel(newLevel)
            }
            else {                                    
            	if (newLevel == 0 && currState == "on") {deviceDMatch.off()}
                else {deviceDMatch.setLevel(newLevel)}
            }      
    	}
	}
	if (deviceType == "cTstat") {
			def deviceTMatch = cTstat.find {s -> s.label.toLowerCase() == deviceD}
		if (deviceTMatch) {
           	def currentMode = deviceTMatch.latestValue("thermostatMode")
          	def currentHSP = deviceTMatch.latestValue("heatingSetpoint") 
           	def currentCSP = deviceTMatch.latestValue("coolingSetpoint") 
           	def currentTMP = deviceTMatch.latestValue("temperature") 
           	def newSetPoint = currentTMP             
				numN = numN < 60 ? 60 : numN >85 ? 85 : numN
            if (unitU == "TEMP") {
            		newSetPoint = numN
                    if (debug) log.debug "Targeted set point is = '${newSetPoint}', current temperature is = '${newSetPoint}'"
            	if (newSetPoint > currentTMP) {
                	if (currentMode == "cool" || currentMode == "off") {
                 		deviceTMatch?."heat"()
                        if (debug) log.debug "Turning heat on because requested temperature of '${newSetPoint}' is greater than current temperature of '${currentTMP}' " 
            		}
                    deviceTMatch?.setHeatingSetpoint(newSetPoint)
                    if (debug) log.debug "Adjusting Heating Set Point to '${newSetPoint}' because requested temperature is greater than current temperature of '${currentTMP}'"
                }
                else if (newSetPoint < currentTMP) {
                    if (currentMode == "heat" || currentMode == "off") {
                    	deviceTMatch?."cool"()
                        if (debug) log.debug "Turning AC on because requested temperature of '${newSetPoint}' is less than current temperature of '${currentTMP}' "    
                    }
					deviceTMatch?.setCoolingSetpoint(newSetPoint)                 
                    if (debug) log.debug "Adjusting Cooling Set Point to '${newSetPoint}' because requested temperature is less than current temperature of '${currentTMP}'"
                }
            }
            if (deviceCommand == "increase") {
            		newSetPoint = currentTMP + cTemperature
                	newSetPoint = newSetPoint < 60 ? 60 : newSetPoint >85 ? 85 : newSetPoint        
                if (currentMode == "cool" || currentMode == "off") {
                	deviceTMatch?."heat"()
                   	if (debug) log.debug "Turning heat on because requested command asked for heat to be set to '${newSetPoint}'" 
                }
           		else {
                	if  (currentHSP < newSetPoint) {
                		deviceTMatch?.setHeatingSetpoint(newSetPoint)
                        thermostat?.poll()
                    	if (debug) log.debug "Adjusting Heating Set Point to '${newSetPoint}'"
                  	}
                    else {
                    	if (debug) log.debug "Not taking action because heating is already set to '${currentHSP}', which is higher than '${newSetPoint}'"  
                	}  
            	}
            }
            if (deviceCommand == "decrease") {
                newSetPoint = currentTMP - cTemperature
                newSetPoint = newSetPoint < 60 ? 60 : newSetPoint >85 ? 85 : newSetPoint     
                if (currentMode == "heat" || currentMode == "off") {
                   	deviceTMatch?."cool"()
                    if (debug) log.debug "Turning AC on because requested command asked for cooling to be set to '${newSetPoint}'"     
                }   	
                else {
                	if (currentCSP < newSetPoint) {
                		deviceTMatch?.setCoolingSetpoint(newSetPoint)
                		thermostat?.poll()
                    	if (debug) log.debug "Adjusting Cooling Set Point to '${newSetPoint}'"
                    }
                    else {
                    	if (debug) log.debug "Not taking action because cooling is already set to '${currentCSP}', which is lower than '${newSetPoint}'"  
                	} 
                }  
              }
         }
	}
}
/************************************************************************************************************
   FEEDBACK HANDLER
************************************************************************************************************/      
def feedbackHandler(data) {   
    def deviceType = data.type
    def deviceCommand = data.command
   	def deviceD = data.device
    def unitU = data.unit
    def numN = data.num
	def pLevel = cLevel
}    
/************************************************************************************************************
   TEXT TO SPEECH PROCESS (PARENT) - Lambda via page t
************************************************************************************************************/
def processTts() {
		def ptts = params.ttstext
		def pttx = params.ttstext 
        def pintentName = params.intentName
            if (debug) log.debug "Message received from Lambda with: (ptts) = '${ptts}', (pintentName) = '${pintentName}'"
        def outputTxt = ''
    	def pContCmds = false
        def pContCmdsR = false
        def dataSet = [ptts:ptts,pttx:pttx,pintentName:pintentName] 
        	def repeat = "repeat last message"
		def whatsUP = "what's up"
                    	log.debug "repeat = ${repeat}"

        	def play = "play message"
                    	log.debug "play = ${play}"

        	def record = ptts.replace("record a message", "")
        				log.debug "record = ${record}"
        	def recordingNow = ptts.startsWith("record a message")
        				log.debug "recordingNow = ${recordingNow}"
      
        if (ptts==repeat || ptts == play  || ptts == whatsUP) {
						childApps.each { child ->
    						def cLast = child.label.toLowerCase()
            				if (cLast == pintentName.toLowerCase()) {
                                def cLastMessage 
                       			def cLastTime
                                pContCmds = child.ContCmds
                                pContCmdsR = child.ContCmdsR
                                if (pContCmdsR == false) {
                                	pContCmds = pContCmdsR
                                }
                                if (ptts == repeat || ptts == whatsUP) {
                                	outputTxt = child.getLastMessage()
                                }
                                else outputTxt = "Your last recording was, " + state.recording
                                if (debug) log.debug "Profile matched is ${cLast}, last profile message was ${outputTxt}" 
                			}
               			}
        }    
		else {
    			if (ptts){
     				state.lastMessage = ptts
                    state.lastIntent = pintentName
                    state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)                   
                    childApps.each {child ->
						if (recordingNow == false) child.profileEvaluate(dataSet)
                        }
            			//Preparing Alexa Response
                        childApps.each { child ->
    						def cm = child.label
            					if (cm.toLowerCase() == pintentName.toLowerCase()) {
                              		def cAcustom = child.Acustom
									def cArepeat = child.Arepeat
									def cAfeedBack = child.AfeedBack
                                    def AprofileMsg = child.AprofileMsg
                                    pContCmds = child.ContCmds
                                    
                                    if (recordingNow == true) {
        								state.recording = record
        								outputTxt = "Ok, message recorded. To play it later, just say: play message to this profile"
                                        pContCmds = false
        							}
                                    if (AprofileMsg == true) {
                                    	outputTxt = child.AprofileMsgTxt
                                        log.info "The child profile message has initiated and the message is: '${child.AprofileMsgTxt}'"
                                        }
     	                			else if (cAfeedBack != true) {
                                		if (cAcustom != false) {
                            				outputTxt = child.outputTxt
                            			}
                            			else {
                        					if (cArepeat == !false || cArepeat == null ) {
                                            	outputTxt = "I have delivered the following message to '${cm}',  " + ptts
                                                if (debug) log.debug "Alexa verbal response = '${outputTxt}'"
											}
                        					else {
                            					outputTxt = "Message sent to ${pintentName}, " 
												if (debug) log.debug "Alexa verbal response = '${outputTxt}'"
           									}
                                		}
                             		}
           						}  
                  		}
				}
      	}
        if (debug) log.debug "Alexa response sent to Lambda = '${outputTxt}', '${pContCmds}' "
		return ["outputTxt":outputTxt, "pContCmds":pContCmds]
}

/******************************************************************************************************
   SPEECH AND TEXT PROCESSING (PROFILE)
******************************************************************************************************/
def profileEvaluate(params) {
        def tts = params.ptts
        def txt = params.pttx
        def intent = params.pintentName        
        def childName = app.label       
        def data = [args: tts ]
        if (intent.toLowerCase() == childName.toLowerCase()){
        	sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")
      		if (parent.debug) log.debug "sendNotificationEvent sent to CoRE was '${app.label}' from the TTS process section"
            if (!disableTts){
        			if (PreMsg) 
        				tts = PreMsg + tts
        				if (parent.debug) log.debug "tts with PreMsg = '${tts}'"
                    else {
            			tts = tts
            			if (parent.debug) log.debug "tts without PreMsg = '${tts}'"
                    }
            			if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
            					if (synthDevice) {
                                synthDevice?.speak(tts) 
        			    			if (parent.debug) log.debug "Sending message to Synthesis Devices"
                                    }
                				if (mediaDevice) {
                                	mediaDevice?.speak(tts) 
                					if (parent.debug) log.debug "Sending message to Media Devices"
                                    }
            						if (tts) {
										state.sound = textToSpeech(tts instanceof List ? tts[0] : tts)
									}
									else {
										state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
                                        if (parent.debug) log.debug "You selected the custom message option but did not enter a message"
									}
								if (sonosDevice) {
									            def currVolLevel = sonosDevice.latestValue("level")
                    						
									if (parent.debug) log.debug "Current volume level is: '${currVolLevel}'"
									def newVolLevel = volume//-(volume*10/100)
									if (parent.debug) log.debug "Attempting to set volume to 10% less than selected volume '${newVolLevel}'"
										sonosDevice.setLevel(newVolLevel)
										sonosDevice.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
                    						if (parent.debug) log.debug "Sending message to Sonos Devices"
                                            }
    								}
    					sendtxt(txt) 
                        state.lastMessage = txt
                        state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
            			if (parent.debug) log.debug "Sending sms and voice message to selected phones and speakers" 
				}
				else {
    					sendtxt(txt)
                        state.lastMessage = txt
                        state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
           					if (parent.debug) log.debug "Only sending sms because disable voice message is ON"
				}
 				if (hues) {               
                colorB() 
               	}
                if (flashSwitches) {
                flashLights()
                }
                profileDeviceControl()
                if (runRoutine) {
                location.helloHome?.execute(settings.runRoutine)
                log.info "Running Routine ${runRoutine}"
                }
                if (runRoutine2) {
                location.helloHome?.execute(settings.runRoutine2)
                log.info "And Running Routine ${runRoutine2}"
                }
				if (newMode) {
				setLocationMode(newMode)
                log.info "The mode has been changed from '${location.mode}' to '${setMode}'"
				}    		
    		}
        }
/***********************************************************************************************************************
    LAST MESSAGE HANDLER - PROFILE
***********************************************************************************************************************/
def getLastMessage() {
	def cOutputTxt = "The last message sent to " + app.label + " was," + state.lastMessage + ", and it was sent at, " + state.lastTime
	return  cOutputTxt 
	if (parent.debug) log.debug "Sending last message to parent '${cOutputTxt}' "
}
/***********************************************************************************************************************
    LAST MESSAGE HANDLER - MAIN
***********************************************************************************************************************/
def getLastMessageMain() {
	def outputTxt = "The last message sent was," + state.lastMessage + ", and it was sent to, " + state.lastIntent + ", at, " + state.lastTime
    return  outputTxt 
  	if (debug) log.debug "Sending last message to Lambda ${outputTxt} "
}
/***********************************************************************************************************************
    EASTER EGGS - PROFILE
***********************************************************************************************************************/
def loveSmartThings() {
	def cOutputTxt = 	"and so do I, With this new version of EchoSistant smart app, we are taking your experience interacting with Alexa and SmartThings, to a new level,"+
    					"You can tell me that it's too dark, or too bright, and I will adjust the light level, just the way you want them to be, with no questions asked"+
                        ", You can also tell me that it's too cold, or too hot, and I will make sure you are as comfortable as possible, in your own home, "+
                        " I can do all of this and so much more, just dare to ask me. Congratulations for installing the new app, "+
                        "and don't forget to let us know what other neat features you would like to see in future versions"
	return  cOutputTxt 
	if (parent.debug) log.debug "Sending last message to parent '${cOutputTxt}' "
}
/***********************************************************************************************************************
 		SKILL DETAILS
 ***********************************************************************************************************************/
def getProfileDetails() {
def c = "" 
def children = getChildApps()	
    children?.each { child -> 
		c +=child.label +"\n" } 

def ProfileDetails = 	" \n" +
					"Listed below you will find, for your quick reference, the LIST_OF_PROFILES required for the Main Intent Skill \n" +
					"\n"+
					"AWS SKILL CUSTOM SLOTS INFORMATION \n " +
                    "\n"+
                    "  LIST_OF_PROFILES \n" +
							"${c}" 
                            				            		
return  ProfileDetails
}

def getDeviceDetails() {
def s = "" 
	cSwitches.each { device -> 
		s +=device.label +"\n" }
def d = "" 
	cDimmers.each { device -> 
		d +=device.label +"\n" }
def G = ""
	cGdoor.each { device ->
    	G +=device.label +"\n" }
def L = ""
	cGlock.each { device ->
    	L +=device.label +"\n" }
def t = "" 
	cTstat.each { device -> 
		t +=device.label +"\n" }
        
def DeviceDetails = 	" \n" +
					"Listed below you will find, for your quick reference, the LIST_OF_DEVICES required for the Main Intent Skill \n" +
                    " \n" +
					"AWS SKILL CUSTOM SLOTS INFORMATION \n " +                    
                    " \n" +
                    "  LIST_OF_DEVICES\n" + 			
                     " \n" +    		
                            "${s} ${d} ${G} ${L} ${t}\n" 
                    
return DeviceDetails
}
/***********************************************************************************************************************
    RESTRICTIONS HANDLER
***********************************************************************************************************************/
private getModeOk() {
    def result = !modes || modes?.contains(location.mode)
	result
} 
private getDayOk() {
    def df = new java.text.SimpleDateFormat("EEEE")
		def timeZone = location.timeZone
        location.timeZone ? df.setTimeZone(location.timeZone) : df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		def day = df.format(new Date())
	    def result = !runDay || runDay?.contains(day)
        def mode = location.mode
        if (debug) log.trace "modeOk = $result; Location Mode is: $mode"
        if (debug) log.trace "getDayOk = $result. Location time zone is: $timeZone"
        return result
}
private getTimeOk() {
	def result = true
	if ((starting && ending) ||
	(starting && endingX in ["Sunrise", "Sunset"]) ||
	(startingX in ["Sunrise", "Sunset"] && ending) ||
	(startingX in ["Sunrise", "Sunset"] && endingX in ["Sunrise", "Sunset"])) {
		def currTime = now()
		def start = null
		def stop = null
		def s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: startSunriseOffset, sunsetOffset: startSunsetOffset)
		if(startingX == "Sunrise") start = s.sunrise.time
		else if(startingX == "Sunset") start = s.sunset.time
		else if(starting) start = timeToday(starting,location.timeZone).time
		s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: endSunriseOffset, sunsetOffset: endSunsetOffset)
		if(endingX == "Sunrise") stop = s.sunrise.time
		else if(endingX == "Sunset") stop = s.sunset.time
		else if(ending) stop = timeToday(ending,location.timeZone).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	if (parent.debug) log.trace "getTimeOk = $result."
    }
    return result
}
private hhmm(time, fmt = "h:mm a") {
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}
private offset(value) {
	def result = value ? ((value > 0 ? "+" : "") + value + " min") : ""
}
private timeIntervalLabel() {
	def result = ""
	if      (startingX == "Sunrise" && endingX == "Sunrise") result = "Sunrise" + offset(startSunriseOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunrise" && endingX == "Sunset") result = "Sunrise" + offset(startSunriseOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunset" && endingX == "Sunrise") result = "Sunset" + offset(startSunsetOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunset" && endingX == "Sunset") result = "Sunset" + offset(startSunsetOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunrise" && ending) result = "Sunrise" + offset(startSunriseOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (startingX == "Sunset" && ending) result = "Sunset" + offset(startSunsetOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (starting && endingX == "Sunrise") result = hhmm(starting) + " to Sunrise" + offset(endSunriseOffset)
	else if (starting && endingX == "Sunset") result = hhmm(starting) + " to Sunset" + offset(endSunsetOffset)
	else if (starting && ending) result = hhmm(starting) + " to " + hhmm(ending, "h:mm a z")
}
/***********************************************************************************************************************
    SMS HANDLER
***********************************************************************************************************************/
private void sendtxt(message) {
def stamp = state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
    if (parent.debug) log.debug "Request to send sms received with message: '${message}'"
    if (sendContactText) {
    	sendNotificationToContacts(message, recipients)
            if (parent.debug) log.debug "Sending sms to selected reipients"
    } 
    else {
    	if (push && parent.timeStamp) {
            sendPush (message + " at " + stamp)
            	if (parent.debug) log.debug "Sending push message to selected reipients with timestamp"
        }
        else {
        if (push) {
        sendPush message
            	if (parent.debug) log.debug "Sending push message to selected reipients"
    			}
            }
        }
     if (notify && parent.timeStamp) {
        	sendNotificationEvent(message + " at " + stamp)
             	if (parent.debug) log.debug "Sending notification to mobile app with timestamp"
				}
                else {
                if (notify) {
                sendNotificationEvent (message)
                if (parent.debug) log.debug "Sending notification to mobile app with timestamp"
                	}
                }
            	    
    if (sms && parent.timeStamp) {
        sendText(sms, message + " at " + stamp)
        if (parent.debug) log.debug "Processing message for selected phones"
		}
        else {
        	if (sms) {
            	sendText(sms, message)
                }
            }    
		}
private void sendText(number, message) {
    if (sms) {
        def phones = sms.split("\\;")
        for (phone in phones) {
            sendSms(phone, message)
            if (parent.debug) log.debug "Sending sms to selected phones"
        }
    }
}

/************************************************************************************************************
   Switch/Color/Dimmer/Toggle Handlers
************************************************************************************************************/
// Used for delayed devices
def turnOnSwitch() { switches?.on() }  
def turnOffSwitch() { switches?.off() }
def turnOnOtherSwitch() { otherSwitch?.on() }
def turnOffOtherSwitch() { otherSwitch?.off() }  
def turnOnDimmers() { def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
			dimmers?.setLevel(dimmersLVL) }
def turnOffDimmers() { dimmers?.off() }
def turnOnOtherDimmers() { def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
			otherDimmers?.setLevel(otherDimmersLVL) }
def turnOffOtherDimmers() { otherDimmers?.off() }            

// Primary control of profile triggered lights/switches when delayed
def profileDeviceControl() {
	if (sSecondsOn) { runIn(sSecondsOn,turnOnSwitch)}
    if (sSecondsOff) { runIn(sSecondsOff,turnOffSwitch)}
    if (sSecondsOn1)  { runIn(sSecondsOn1,turnOnOtherSwitch)}
    if (sSecondsOff1) { runIn(sSecondsOff1,turnOffOtherSwitch)}
	if (sSecondsOn2) { runIn(sSecondsOn2,turnOnDimmers)}
	if (sSecondsOff2) { runIn(sSecondsOff2,turnOffDimmers)}
    if (sSecondsOn3) { runIn(sSecondsOn3,turnOnOtherDimmers)}
	if (sSecondsOff3) { runIn(sSecondsOff3,turnOffOtherDimmers)}

// Control of Lights and Switches when not delayed            
    if (!sSecondsOn) {
            	if  (switchCmd == "on") { switches?.on() }
	            	else if (switchCmd == "off") { switches?.off() }
	           		if (switchCmd == "toggle") { toggle() }
                if (otherSwitchCmd == "on") { otherSwitch?.on() }
            		else if (otherSwitchCmd == "off") { otherSwitch?.off() }
                    if (otherSwitchCmd == "toggle") { toggle() }
                if (dimmersCmd == "set" && dimmers) { def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
        				dimmers?.setLevel(level) }
            	if (otherDimmersCmd == "set" && otherDimmers) { def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
        				otherDimmers?.setLevel(otherlevel) }
                }
			}

private colorB() { 
	if (hueCmd == "off") { hues?.off() }
    if (hueCmd1 == "off") { hues1?.off() }
		if (debug) log.debug "color bulbs initiated"
		def hueColor = 0
        def hueColor1 = 0
        fillColorSettings()
        if (color == "White")hueColor = 48
        if (color == "Red")hueColor = 0
        if (color == "Blue")hueColor = 70//60  
        if (color == "Green")hueColor = 39//30
        
        if(color == "Yellow")hueColor = 25//16
        if(color == "Orange")hueColor = 11
        if(color == "Purple")hueColor = 75
        if(color == "Pink")hueColor = 83
        
        
        
	def colorB = [hue: hueColor, hue1: hueColor1, saturation: 100, level: (lightLevel as Integer) ?: 100]
    hues*.setColor(colorB)
	}
 def fillColorSettings(){
	def colorData = []
    colorData << [name: "White", hue: 0, sat: 0] << [name: "Orange", hue: 11, sat: 100] << [name: "Red", hue: 100, sat: 100] << [name: "Purple", hue: 77, sat: 100]
    colorData << [name: "Green", hue: 30, sat: 100] << [name: "Blue", hue: 66, sat: 100] << [name: "Yellow", hue: 16, sat: 100] << [name: "Pink", hue: 95, sat: 100]
    colorData << [name: "Cyan", hue: 50, sat: 100] << [name: "Chartreuse", hue: 25, sat: 100] << [name: "Teal", hue: 44, sat: 100] << [name: "Magenta", hue: 92, sat: 100]
	colorData << [name: "Violet", hue: 83, sat: 100] << [name: "Indigo", hue: 70, sat: 100]<< [name: "Marigold", hue: 16, sat: 75]<< [name: "Raspberry", hue: 99, sat: 75]
    colorData << [name: "Fuchsia", hue: 92, sat: 75] << [name: "Lavender", hue: 83, sat: 75]<< [name: "Aqua", hue: 44, sat: 75]<< [name: "Amber", hue: 11, sat: 75]
    colorData << [name: "Carnation", hue: 99, sat: 50] << [name: "Periwinkle", hue: 70, sat: 50]<< [name: "Pistachio", hue: 30, sat: 50]<< [name: "Vanilla", hue: 16, sat: 50]
    if (customName && (customHue > -1 && customerHue < 101) && (customSat > -1 && customerSat < 101)) colorData << [name: customName, hue: customHue as int, sat: customSat as int]
    return colorData    
}    
    
private toggle() {
    if (parent.debug) log.debug "The selected device is toggling now"
	if (switches) {
	if (switches?.currentValue('switch').contains('on')) {
		switches?.off()
		}
    else if (switches?.currentValue('switch').contains('off')) {
		switches?.on()
		}
    }
    if (otherSwitch) {
	if (otherSwitch?.currentValue('switch').contains('on')) {
		otherSwitch?.off()
	}
	else if (otherSwitch?.currentValue('switch').contains('off')) {
		otherSwitch?.on()
		}
	}
	if (lock) {
	if (lock?.currentValue('lock').contains('locked')) {
		lock?.unlock()
		}
    }
	if (parent.debug) log.debug "The selected device has toggled"
}
/************************************************************************************************************
   Flashing Lights Handler
************************************************************************************************************/
private flashLights() {
 	if (parent.debug) log.debug "The Flash Switches Option has been activated"
	def doFlash = true
	def onFor = onFor ?: 60000/60
	def offFor = offFor ?: 60000/60
	def numFlashes = numFlashes ?: 3
	if (state.lastActivated) {
		def elapsed = now() - state.lastActivated
		def sequenceTime = (numFlashes + 1) * (onFor + offFor)
		doFlash = elapsed > sequenceTime
	}
	if (doFlash) {
		state.lastActivated = now()
		def initialActionOn = flashSwitches.collect{it.currentflashSwitch != "on"}
		def delay = 0L
		numFlashes.times {
			flashSwitches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.on(delay: delay)
				}
				else {
					s.off(delay:delay)
				}
			}
			delay += onFor
			flashSwitches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.off(delay: delay)
				}
				else {
					s.on(delay:delay)
				}
			}
			delay += offFor
		}
	}
}
/************************************************************************************************************
   Alerts Handler
************************************************************************************************************/
def alertsHandler(evt) {
	def eVal = evt.value
    def eName = evt.name
    def eDev = evt.device
    def stamp = state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)  
    def eTxt = " "
    if (debug) log.debug "Received event name ${evt.name} with value:  ${evt.value}, from: ${evt.device}"

	if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
        if (eVal == "on") { 
            		if (audioTextOn) {
                	eTxt = txtFormat(audioTextOn, eDev, eVal)
                	if (debug) log.debug "Received event: on, playing message:  ${eTxt}"
                    if(speech1) speech1?.speak (eTxt)
                    if (music1) {
                        playAlert(eTxt, music1)
                    }
                    if (timeStamp) {
                    if (push1) sendPush eTxt + stamp
					if (notify1) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                    if (push1) sendPush eTxt
					if (notify1) sendNotificationEvent (eTxt)
                    }
        	}
        }
        if (eVal == "off") {       
                if (audioTextOff) {
                eTxt = txtFormat(audioTextOff, eDev, eVal)
                if (debug) log.debug "Received event: off, playing message:  ${eTxt}"
                speech1?.speak(eTxt)
                    if (music1) {
                        playAlert(eTxt, music1)
                    }
                }
                    if (timeStamp) {
                    if (push1) sendPush eTxt + stamp
					if (notify1) sendNotificationEvent (eTxt + stamp)
                	}
                    else {                
                    if (push1) sendPush eTxt
					if (notify1) sendNotificationEvent (eTxt)
             }
        }
        if (eVal == "open") {     
            	if (audioTextOpen) {
               eTxt = txtFormat(audioTextOpen, eDev, eVal)
               if (debug) log.debug "Received event:open, playing message:  ${eTxt}"
            speech2?.speak(eTxt)
                if (music2) {
                playAlert(eTxt, music2)
				}
                    if (timeStamp) {
                    if (push2) sendPush eTxt + stamp
					if (notify2) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                	if (push2) sendPush eTxt
					if (notify2) sendNotificationEvent (eTxt)
					} 
 	           }
            }
            if (eVal == "closed") {           
                	if (audioTextClosed) {
                eTxt = txtFormat(audioTextClosed, eDev, eVal)
                if (debug) log.debug "Received event closed, playing message:  ${eTxt}"
                speech2?.speak(eTxt)
                if (music2) {
                    playAlert(eTxt, music2)
                    if (timeStamp) {
                    if (push2) sendPush eTxt + stamp
					if (notify2) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                    if (push2) sendPush eTxt
					if (notify2) sendNotificationEvent (eTxt)
							}
                        }
					}
                }
        if 	(eVal == "locked") {         
				if (audioTextLocked) {
                eTxt = txtFormat(audioTextLocked, eDev, eVal)
            speech3?.speak(eTxt)
                    if (music3) {
                        playAlert(eTxt, music3)
 
					}
                    if (timeStamp) {
                    if (push3) sendPush eTxt + stamp
					if (notify3) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                	if (push3) sendPush eTxt
					if (notify3) sendNotificationEvent (eTxt)                    
                	}
                }
            }
            if (eVal == "unlocked") {        
                    if (audioTextUnlocked) {
                        eTxt = txtFormat(audioTextUnlocked, eDev, eVal)
                        speech3?.speak(eTxt)
                            if (music3) {
                                playAlert(eTxt, music3)
                                }
                    if (timeStamp) {
                    if (push3) sendPush eTxt + stamp
					if (notify3) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                   	if (push3) sendPush eTxt
					if (notify3) sendNotificationEvent (eTxt)                            
                    	}        
                    }
               }
        if (eVal == "active") {         
				if (debug) log.debug "Received Motion Event but Motion Alerts are turned off"
            		if (audioTextActive) { 
            			eTxt = txtFormat(audioTextActive, eDev, eVal)
            			if (debug) log.debug "Received event Active, playing message:  ${eTxt}"
            			speech4?.speak(eTxt)
                    		if (music4) {
                        		playAlert(eTxt, music4)
                    		} 
                    if (timeStamp) {
                    if (push4) sendPush eTxt + stamp
					if (notify4) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                   	if (push4) sendPush eTxt
					if (notify4) sendNotificationEvent (eTxt)                            
                	}
            	}
            }
            if (eVal == "inactive")  {         
				if (debug) log.debug "Received Motion Event but Motion Alerts are turned off"
					if (audioTextInactive) {
                		eTxt = txtFormat(audioTextInactive, eDev, eVal)
                		if (debug) log.debug "Received event Inactive, playing message:  ${eTxt}"
                			speech4?.speak(eTxt)
                    		if (music4) {
                        		playAlert(eTxt, music4)
                    		} 
                    if (timeStamp) {
                    if (push4) sendPush eTxt + stamp
					if (notify4) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                   	if (push4) sendPush eTxt
					if (notify4) sendNotificationEvent (eTxt)                     	
                        }
                	}
        		}
        if (eVal == "present") {          
				if (debug) log.debug "Received Presence Event but Presence Alerts are turned off"
				if (audioTextPresent) {
                eTxt = txtFormat(audioTextPresent, eDev, eVal)        
            if (debug) log.debug "Received event Present, playing message:  ${eTxt}"
            speech5?.speak(eTxt)
                    if (music5) {
                        playAlert(eTxt, music5)
                    } 
                    if (timeStamp) {
                    if (push5) sendPush eTxt + stamp
					if (notify5) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                   	if (push5) sendPush eTxt
					if (notify5) sendNotificationEvent (eTxt) 
					}
                }
            }
        if (eVal == "not present")  {        
				if (debug) log.debug "Received Presence Event but Presence Alerts are turned off"
				if (audioTextNotPresent) {
                eTxt = txtFormat(audioTextNotPresent, eDev, eVal)            
                if (debug) log.debug "Received event Not Present, playing message:  ${eTxt}"
                speech5?.speak(eTxt)
                    if (music5) {
                        playAlert(eTxt, music5)
                    } 
                    if (timeStamp) {
                    if (push5) sendPush eTxt + stamp
					if (notify5) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                   	if (push5) sendPush eTxt
					if (notify5) sendNotificationEvent (eTxt) 
						}
                    }
                }
        if (eName == "heatingSetpoint")  {                    
            if (audioTextHeating) {
            	def eTemp = eVal
                eTemp = eTemp.replace('.0', " ")
            	eTxt = txtFormat(audioTextHeating, eDev, eTemp)            
            	if (debug) log.debug "Received event heatingSetpoint, playing message:  ${eTxt}"
                speech8?.speak(eTxt)
                    if (music8) {
                        playAlert(eTxt, music8)
                    } 
                    if (timeStamp) {
                    if (push8) sendPush eTxt + stamp
					if (notify8) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                    if (push8) sendPush eTxt
					if (notify8) sendNotificationEvent (eTxt) 
                	}
                }
            }
            if (eName == "coolingSetpoint") {
				if (audioTextCooling) {
            		def eTemp = eVal
                	eTemp = eTemp.replace('.0', " ")
                	eTxt = txtFormat(audioTextCooling, eDev, eTemp)            
                	if (debug) log.debug "Received event coolingSetpoint, playing message:  ${eTxt} "
                	speech8?.speak(eTxt)
                	if (music8) {
                        playAlert(eTxt, music8)
                    }
                    if (timeStamp) {
                    if (push8) sendPush eTxt + stamp
					if (notify8) sendNotificationEvent (eTxt + stamp)
                	}
                    else {
                     if (push8) sendPush eTxt
					if (notify8) sendNotificationEvent (eTxt) 
                }
            }  
        }   
	}
}

private txtFormat (message, eDev, eVal) {
    def eTxt = " " 
        if(message) {
        	message = message.replace('$device', "${eDev}")
        	message = message.replace('$action', "${eVal}")
	  	    eTxt = message
        }  	
            if (debug) log.debug "Processed Alert: ${eTxt} "
    		
            return eTxt
}



/************************************************************************************************************
   Play Sonos Alert
************************************************************************************************************/
def playAlert(message, speaker) {
    state.sound = textToSpeech(message instanceof List ? message[0] : message)
    speaker.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
    if (debug) log.debug "Sending message: ${message} to speaker: ${speaker}"

}
/************************************************************************************************************
   Page status and descriptions 
************************************************************************************************************/       
//go to Parent set up 
def completeProfiles(){
    def result = ""
    if (childApps.size()) {
    	result = "complete"	
    }
    result
}
def profilesDescr() {
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
def completeSettings(){
    def result = ""
    def ParConC = completeParCon()
    def AlertProC = completeAlertPro()
    
    log.debug "ParConC = ${ParConC}, AlertProC = ${ParConC}"
    if (ParConC || AlertProC ) {
        result = "complete"	
    }
    result
}
def settingsDescr() {
    def text = "Tap here to configure settings" 
    def completeSettings = completeSettings()
    if (completeSettings && !allNotifications) {
    	text = "Configured but all notifications have been disabled"
    }
    else text = "Configured"
	text	
}
def supportDescrL() {
	def text = 	"SmartThings Details \n" +
    			"  Version = ${textVersion()} \n" +
        		"  Release # = ${textRelease()} \n"+
    			"  Date = ${dateRelease()} \n"+
    			"AWS Lambda Details \n" +
    			"  Version = ${state.lambdatextVersion} \n" +
                "  Release # = ${state.lambdaReleaseTxt} \n"+
    			"  Date = ${state.lambdaReleaseDt} \n"
                }
def DevProDescr() {
    def text = "Tap here to Configure"
    if (switches || dimmers || hues ||runRoutine || flashSwitches || newMode) { 
            text = "Configured" //"These devices will execute: ${switches}, ${dimmers}. Tap to change device(s)" 
    }
    text
}
def DevConDescr() {
	def text = "Tap to set"
     if (switches || dimmers || hues || flashSwitches)
     { 
            text = "Configured" //"These devices will execute: ${switches}, ${dimmers}. Tap to change device(s)"
            }
    text   
}
def ParConDescr() {
	def text = "Tap to set"
    def ParConD = " "
     if (cSwitches || cDimmers || cTstat || cLock || cDoors)
     { 
            ParConD = "conf"
            text = "Configured" //"These devices will execute: ${switches}, ${dimmers}. Tap to change device(s)"
            }
    text   
}       
def completeParCon() {
    def result = ""
    def ParConC = ""
    if (cSwitches || cDimmers || cTstat || cLock || cDoors) { 
       ParConC = "comp"
       result = "complete"
    }
    result
}
//go to Profile set up
def completeMsgPro(){
    def result = ""
    if (synthDevice || sonosDevice) {
    	result = "complete"	
    }
    result
}
def MsgProDescr() {
    def text = "Tap here to Configure"
    if (synthDevice || sonosDevice) {
        if (synthDevice && !sonosDevice) {   
            text = "Configured"//"Using: ${synthDevice}. Tap to change device(s)" 
        }
        if (!synthDevice && sonosDevice) {
            text = "Configured" //"Using: ${sonosDevice}. Tap to change device(s)" 
        }
        if (synthDevice && sonosDevice) {
            text = "Configured" //"Using: ${synthDevice} AND ${synthDevice}. Tap to change device(s)" 
        }
     }
    text
}
def completeSMS(){
    def result = ""
    if (sendText || sms  || push == "true" || notify == "true") {
    	result = "complete"	
    }
    result
}
def SMSDescr() {
    def text = "Tap here to Configure"
    if (sendText || sms  || push == "true" || notify == "true") {
            text = "Configured" //"Using this contact(s): ${recipients}. Tap to change" 
     }
    text
}

def completeGroups(){
    def result = ""
    if (gSwitches) {
    	result = "complete"	
    }
    result
}
def groupsDescr() {
    def text = "Tap here to Configure"
    if (gSwitches) {
            text = "Configured" //"Using this contact(s): ${recipients}. Tap to change" 
     }
    text
}

def completeDevPro(){
    def result = ""
    if (switches || dimmers || runRoutine || hues || flashSwitches || newMode) {
    	result = "complete"	
    }    
    result
}
def completeDevCon() {
    def result = ""
    if (switches || dimmers || hues || flashSwitches) { 
       result = "complete"
    }
    result
}
def completeAlertPro(){
	def result = ""
    def AlertProC = ""
	if (speech1 || push1 || notify1 || music1 || speech2 || push2 || notify2 || music2 || speech3 || push3 || notify3 || music3 || speech4 || push4 || notify4 || music4 || speech5 || push5 || notify5 || music5 || speech8 || push8 || notify8 || music8) 
    {
    	AlertProC = "comp"
        result = "complete"
    }    	
    	result
}
def AlertProDescr() {
    def text = "Tap here to Configure"
	def completeAlertPro = completeAlertPro()
    if (completeAlertPro && !allNotifications ) 
    {
        text = "Configured but all Notifications have been disabled"
    }
    else text = "Configured"
	    text
}
def completeMsgConfig(){
    def result = ""
    if (ShowPreMsg || Acustom || Arepeat || AfeedBack || disableTts || ContCmds || ContCmdsR || modes || runDay) {
    	result = "complete"	
    }
    result
}
def MsgConfigDescr() {
    def text = "Tap here to Configure"
	if (ShowPreMsg || Acustom || Arepeat || AfeedBack || disableTts || ContCmds || ContCmdsR || modes || runDay) {
    	text = "Configured with Message Options"
        }
        if (getDayOk()==false || getModeOk()==false || getTimeOk()==false) {
            text = "Configured with restrictions. Tap to change" 
        }
    text
}
def runRoutineDescr() {
    def text = "Tap here to Configure"
    if (runRoutine) { 
            text = "Configured" //"These devices will execute: ${switches}, ${dimmers}. Tap to change device(s)" 
    }
    text
}
def completeDevicesControlCustom() {
    def result = ""
    if (custSwitch1) {
    	result = "complete"	
    }
    result
}

def devicesControlCustomDescr(deviceName) {
    def text = "Tap here to Configure"
    if (custSwitch1) { 
            text = "Configured" //"These devices will execute: ${switches}, ${dimmers}. Tap to change device(s)" 
    }
    text
}

/************************************************************************************************************
   Misc. Text fields
************************************************************************************************************/
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
private textProfiles() {
def text = childApps.size()     
}
private def textHelp() {
	def text =
		"This smartapp allows you to use an Alexa device to generate a voice or text message on on a different device"
        "See our Wikilinks page for user information!"
		}

/************************************************************************************************************
   Version/Copyright/Information/Help
************************************************************************************************************/
private def textAppName() {
	def text = app.label // Parent Name
}	
private def textVersion() {
	def text = "3.0"
}
private def textRelease() {
	def text = "3.16"
}
private def textReleaseNotes() {
	def text = "See the Wiki"
}
private def dateRelease() {
	def text = "01/01/2017"
}
private def textCopyright() {
	def text = "       Copyright © 2016 Jason Headley"
}
private def textCurrentMode() {
	def text = "                     The current Mode is \n" +
    "                            '${location.currentMode}'"
}
