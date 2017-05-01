/**
 *  Zwave Thermostat Manager - EchoSistant Add-on
 *
 *		4/1/2017		Version:4.0 R.0.0.4		Added WebCoRE integration
 *		3/25/2017		Version:4.0 R.0.0.3		Added Reporting features
 *		3/18/2017		Version:4.0 R.0.0.1		Modified and Released as an EchoSistant Add-On Module
 *
 * 
 * Credits and Kudos: 	this app is largely based on the more popular Thermostat Director SA by Tim Slagle - 
 * 						many thanks to @slagle for his continued support. 
 * 						Without his brilliance, this app would not exist!
 * 
 * 
 *  Copyright 2015 SmartThings
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
 */
/**********************************************************************************************************************************************/ 
definition(
	name: "ThermoManager",
	namespace: "Echo",
	author: "Bobby Dobrescu",
	description: "Adjust zwave thermostats based on a temperature range of a specific temperature sensor",
    parent: "Echo:EchoSistant", 
	category: "My apps",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png"
)
/**********************************************************************************************************************************************/
private release() {
	def text = "R.0.0.4"
}
preferences {
    page name:"pageSetup"
    page name:"TemperatureSettings"
    page name:"ThermostatandDoors"
    page name:"ThermostatAway"
    page name:"reporting"
    page name: "report"
    page name:"Settings"

}

// Show setup page
def pageSetup() {

    def pageProperties = [
        name:       "pageSetup",
        title:      "",
        nextPage:   null,
        install:    true,
        uninstall:  true
    ]

	return dynamicPage(pageProperties) {
		section ("Name (rename) this Profile") {
 		   	label title:"Profile Name ", required:false, defaultValue: "Climate Control Profile"  
		}
        section("General Settings") {
            href "TemperatureSettings", title: "Ambiance", description: TemperatureSettingsParams(), state:greyedOut()
            href "ThermostatandDoors", title: "Disabled Mode", description: ThermostatandDoorsParams(), state: greyedOutDoors()
            href "ThermostatAway", title: "Away Mode", description: ThermostatAwayParams(), state: greyedOutAway()
			href "Settings", title: "Other Settings", description: SettingsParams(), state: greyedOutSettings()
            href "reporting", title: "Available Reports", description: "", state: null
         }

    }
}

// Page - Temperature Settings	
def TemperatureSettings() {
    def sensor = [
        name:       "sensor",
        type:       "capability.temperatureMeasurement",
        title:      "Which Temperature Sensor(s)?",
        multiple:   true,
        required:   false
    ]
    def thermostat = [
        name:       "thermostat",
        type:       "capability.thermostat",
        title:      "Which Thermostat?",
        multiple:   false,
        required:   true
    ]
    def setLow = [
        name:       "setLow",
        type:       "number",
        title:      "Low temp?",
        required:   true
    ]
    
    def cold = [
        name:       "cold",
        type:       "enum",
        title:		"Mode?",
    	required:   false,
		metadata:   [values:["auto", "heat", "cool", "off"]]

    ]

    def SetHeatingLow = [
        name:       "SetHeatingLow",
        type:       "number",
        title:		"Heating Temperature (degrees)",
        required:   true
    ]
    
     def SetCoolingLow = [
        name:       "SetCoolingLow",
        type:       "number",
        title:		"Cooling Temperature (degrees)",
        required:   false
    ]   
    
    def setHigh = [
        name:       "setHigh",
        type:       "number",
        title:      "High temp?",
        required:   true
    ]
    
    def hot = [
        name:       "hot",
        type:       "enum",
        title:		"Mode?",
        required:   false,
        metadata:   [values:["auto", "heat", "cool", "off"]]
    ]
    
    def SetHeatingHigh = [
        name:       "SetHeatingHigh",
        type:       "number",
        title:		"Heating Temperature (degrees)",
        required:   false
    ]
    
     def SetCoolingHigh = [
        name:       "SetCoolingHigh",
        type:       "number",
        title:		"Cooling Temperature (degrees)",
        required:   true
    ] 
  
    def pageName = "Ambiance"
    
    def pageProperties = [
        name:       "TemperatureSettings",
        title:      "",
        //nextPage:   "ThermostatandDoors"
    ]
    
    return dynamicPage(pageProperties) {
        section("Select the main thermostat") {
			input thermostat
		}
        section("Use remote sensors to adjust the thermostat (by default the app is using the internal sensor of the thermostat)") {
			input "remoteSensors", "bool", title: "Enable remote sensor(s)", required: false, defaultValue: false, submitOnChange: true
			if (remoteSensors) {
            	input sensor 
            	if (sensor) {
                    def tempAVG = getAverageUI()
                    paragraph "If multiple sensors are selected, the average temperature is automatically calculated. Current temperature ${tempAVG}"	
				}
            }
        }
		section("When the temperature falls below this temperature (Low Temperature)..."){
			input setLow
		}
        section("Adjust the thermostat to the following settings:"){
			input cold
            input SetHeatingLow
            input SetCoolingLow
		}
        section("When the temperature raises above this temperature (High Temperature)..."){
			input setHigh
		}
        section("Adjust the thermostat to the following settings:"){
			input hot
            input SetCoolingHigh
            input SetHeatingHigh
		}
        section("When temperature is neutral (between above Low and High Temperatures..."){
			input "neutral", "bool", title: "Turn off the thermostat", required: false, defaultValue: false
		}
    }  
}

// Page - Disabled Mode
def ThermostatandDoors() {
  
    def doors = [
        name:       "doors",
        type:       "capability.contactSensor",
        title:      "Which Sensor(s)?",
        multiple:	true,
        required:   false
    ]
    
    def turnOffDelay = [
        name:       "turnOffDelay",
        type:       "decimal",
        title:		"Number of minutes",
        required:	false
    ]
    
    def resetOff = [
        name:       "resetOff",
        type:       "bool",
        title:		"Reset Thermostat Settings when all Sensor(s) are closed",
        required:	false,
        defaultValue: false
    ]
       
    def pageName = "Thermostat and Doors"
    
    def pageProperties = [
        name:       "ThermostatandDoors",
        title:      "",
        //nextPage:   "ThermostatAway"
    ]

    return dynamicPage(pageProperties) {
        section("When one or more contact sensors open, then turn off the thermostat") {
			input doors
		}
		section("Wait this long before turning the thermostat off (defaults to 1 minute)") {
			input turnOffDelay
            input resetOff 
		}
    }
    
}

// Page - Thermostat Away
def ThermostatAway() {

    def modes2 = [
        name:		"modes2", 
        type:		"mode", 
        title: 		"Which Location Mode(s)?", 
        multiple: 	true, 
        required: 	false
    ]
    
    def away = [
        name:       "away",
        type:       "enum",
        title:		"Mode?",
        metadata:   [values:["auto", "heat", "cool", "off"]], 
        required: 	false
    ]

    def setAwayLow = [
        name:       "setAwayLow",
        type:       "decimal",
        title:      "Low temp?",
        required:   false
    ]
    
    def AwayCold = [
        name:       "AwayCold",
        type:       "enum",
        title:		"Mode?",
        metadata:   [values:["auto", "heat", "cool", "off"]],
        required: 	false,
    ]
    
    def setAwayHigh = [
        name:       "setAwayHigh",
        type:       "decimal",
        title:      "High temp?",
		required:   false
    ]
    
    def AwayHot = [
        name:       "AwayHot",
        type:       "enum",
        title:		"Mode?",
        required: 	false,
metadata:   [values:["auto", "heat", "cool", "off"]]
    ]
    
    def SetHeatingAway = [
        name:       "SetHeatingAway",
        type:       "number",
        title:		"Heating Temperature (degrees)",
        required: 	false
    ]   
    
    def SetCoolingAway = [
        name:       "SetCoolingAway",
        type:       "number",
        title:		"Cooling Temperature (degrees)",
        required: 	false
    ]     
    
    def fanAway = [
        name:       "fanAway",
        type:       "enum",
        title:		"Fan Mode?",
        metadata:   [values:["fanAuto", "fanOn", "fanCirculate"]],
        required: 	false
    ]

    def pageName = "Thermostat Away"
    
    def pageProperties = [
        name:       "ThermostatAway",
        title:      "",
        //nextPage:   "Settings"
    ]

    return dynamicPage(pageProperties) {
		
        section("When the Location Mode changes to 'Away'") {
   			input modes2
        }
           
        section("Adjust the thermostat to the following settings:") {
    		input away
            input fanAway                        
            input SetHeatingAway
			input SetCoolingAway
  		}	
        section("If the temperature falls below this temperature while away..."){
			input setAwayLow
		}
        
        section("Automatically adjust the thermostat to the following operating mode..."){
			input AwayCold
		}
        
        section("If the temperature raises above this temperature while away..."){
			input setAwayHigh
    	}
        section("Automatically adjust the thermostat to the following operating mode..."){
			input AwayHot
    	} 
	 }
}
// Show "Reporting" page
def reporting(){
	def report
	return dynamicPage(
    	name		: "reporting"
        ,title		: "Operating reports"
        ,install	: false
        ,uninstall	: false
        ){
    		section(){
            	report = "General Settings"
   				href( "report"
					,title		: report
					,description: ""
					,state		: null
					,params		: [rptName:report]
				) 
                report = "Current state"
                href( "report"
					,title		: report
					,description: ""
					,state		: null
					,params		: [rptName:report]
				)   
                report = "Last results"
                href( "report"
					,title		: report
					,description: ""
					,state		: null
					,params		: [rptName:report]
				)
                report = "Historical results"
                href( "report"
					,title		: report
					,description: ""
					,state		: null
					,params		: [rptName:report]
				)  
                
            }
   }
}
def report(params){
	def reportName = params.rptName
    def reportDetails
	return dynamicPage(
    	name		: "report"
        ,title		: reportName
        ,install	: false
        ,uninstall	: false
        ){
    		section(){
   				paragraph(getReport(reportName)) //"Historical results", "Last results", "Current state", "General Settings"
            }
   }
}


// Show "Setup" page
def Settings() {
 
    def days = [
        name:       "days",
        type:       "enum",
        title:      "Only on certain days of the week",
        multiple:   true,
        required:   false,
        options: 	["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
    ]
    
    def modes = [
        name:		"modes", 
        type:		"mode", 
        title: 		"Only when mode is", 
        multiple: 	true, 
        required: 	false
    ]
    
    def pageName = ""
    
    def pageProperties = [
        name:       "Settings",
        title:      "",
        //nextPage:   "pageSetup"
    ]

    return dynamicPage(pageProperties) {    
       	section ("With these output methods" , hideWhenEmpty: true) {    
                input "sonos", "capability.musicPlayer", title: "On this Music Player", required: false, multiple: true, submitOnChange: true
                    if (sonos) {
                        input "sonosVolume", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    	input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                        input "sonosDelay", "number", title: "(Optional) Delay second delivery of second message by...", description: "seconds", required: false
                    }
                input "speechSynth", "capability.speechSynthesis", title: "On this Speech Synthesis Device", required: false, multiple: true, submitOnChange: true
                        if (speechSynth) {
                            input "speechVolume", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    }
                href "SMS", title: "Send SMS & Push Messages...", description: pSendComplete(), state: pSendSettings()
            }      
        section(title: "Restrictions", hideable: true) {
			href "timeIntervalInput", title: "Only during a certain time", description: getTimeLabel(starting, ending), state: greyedOutTime(starting, ending), refreshAfterSelection:true
			input days
			input modes
        }
		section(title: "Debug") {     
        	input "debug", "bool", title: "Enable debug messages in IDE for troubleshooting purposes", required: false, defaultValue: false, refreshAfterSelection:true
        	input "info", "bool", title: "Enable info messages in IDE to display actions in Live Logging", required: false, defaultValue: false, refreshAfterSelection:true
        } 
		section(title: "Reporting") {     
        	input "useReports", "bool", title: "Enable utilization Reporting", required: false, defaultValue: false, refreshAfterSelection:true
			input "power", "capability.powerMeter", title: "Choose Power Meter", required: false, multiple: false, submitOnChange: true        
           	if(power) input "cost", "decimal", title: "Electricity Cost", description: "0.12", defaultValue: 0.12, required: false

        
        
        }         
    }
}
page name: "SMS"
    def SMS(){
        dynamicPage(name: "SMS", title: "Send SMS and/or Push Messages...", uninstall: false) {
        section ("Push Messages") {
            input "push", "bool", title: "Send Push Notification...", required: false, defaultValue: false
            input "timeStamp", "bool", title: "Add time stamp to Push Messages...", required: false, defaultValue: false  
            }
        section ("Text Messages" , hideWhenEmpty: true) {
            input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true
                if (sendContactText){
                    input "recipients", "contact", title: "Send text notifications to...", multiple: true, required: false
                }
            input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true      
                if (sendText){      
                    paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                    input name: "sms", title: "Send text notification to...", type: "phone", required: false
                }
            }    
        }        
    }
def installed(){
	log.debug "Installed with settings: ${settings}, current app version: ${release()}"
    state.NotificationRelease = "Climate Control: " + release()
	init()
}

def updated(){
	log.debug "Updated with settings: ${settings}, current app version: ${release()}"
	state.NotificationRelease = "Climate Control: " + release()
	unsubscribe()
	init()
}
def init(){
    state.lastStatus = null
    state.sound
    state.lastPlayed
	state.firstCheck = true
    runIn(60, "temperatureHandler")
    	if (debug) log.debug "Temperature will be evaluated in one minute"
     	if(sensor) {
        	subscribe(sensor, "temperature", temperatureHandler)
        }
        else {
        	subscribe(thermostat, "temperature", temperatureHandler)
    	}
    if(modes2){
    	subscribe(location, modeAwayChange)
        	if(sensor) {
            	subscribe(sensor, "temperature", modeAwayTempHandler)
            }
            else {
        		subscribe(thermostat, "temperature", modeAwayTempHandler)
           	}
    
    }
    if(doors){
            subscribe(doors, "contact.open", temperatureHandler)
            subscribe(doors, "contact.closed", doorCheck)
            state.disabledTemp = null
    		state.disabledMode = null
    		state.disableHSP = null 
    		state.disableCSP = null
	}
    //Reporting Data
    if(useReports) {
        subscribe(thermostat, "thermostatMode", checkNotify)
        subscribe(thermostat, "thermostatFanMode", checkNotify)
        subscribe(thermostat, "thermostatOperatingState", checkNotify)
        subscribe(thermostat, "heatingSetpoint", checkNotify)
        subscribe(thermostat, "coolingSetpoint", checkNotify)
        //tempSensors
        subscribe(sensor, "temperature", checkNotify)
        //init state vars
        state.mainState = state.mainState ?: getNormalizedOS(thermostat.currentValue("thermostatOperatingState"))
        state.mainMode = state.mainMode ?: getNormalizedOS(thermostat.currentValue("thermostatMode"))
        state.mainCSP = state.mainCSP ?: thermostat.currentValue("coolingSetpoint").toFloat()
        state.mainHSP = state.mainHSP ?: thermostat.currentValue("heatingSetpoint").toFloat()
        state.startTemp
        state.endTemp
		state.startWattage
        state.endWattage
        if(sensor){
            def tempAVG = sensor ? getAverage(sensor, "temperature") : "undefined device"
            state.mainTemp = tempAVG
        }
        else state.mainTemp = state.mainTemp ?: thermostat.currentValue("temperature").toFloat()
        state.avgConsH
        state.avgConsC
        state.startTime
        state.endTime
        state.dailyUseH
        state.monthlyUseH
        state.dailyUseC
        state.monthlyUseC
        state.mainStatePrior
        checkNotify(null)    
	}

}

def checkNotify(evt){
	def tempStr = ''
    def tempFloat = 0.0
	def rtm
	def currDate = new Date(state.endTime).format("EEEE, MMMM d, yyyy", location.timeZone)
	def currMonth = new Date(state.endTime).format("MM", location.timeZone) as int
    def today = new Date(now()).format("EEEE, MMMM d, yyyy", location.timeZone)
    def thisMonth = new Date(now()).format("MM", location.timeZone) as int    
    def tempAVG = sensor ? getAverage(sensor, "temperature") : null
    def mainTemp = tempAVG.toFloat()
    //thermostat state
	tempStr = getNormalizedOS(thermostat.currentValue("thermostatOperatingState"))
	def mainState = state.mainState
    def mainStateChange = mainState != tempStr
    mainState = tempStr
    //thermostate mode
    tempStr = getNormalizedOS(thermostat.currentValue("thermostatMode"))
    def mainMode = state.mainMode
    def mainModeChange = mainMode != tempStr
    mainMode = tempStr
	//cooling set point
    def mainCSPChange = false
    def mainCSP
	tempFloat = thermostat.currentValue("coolingSetpoint").toFloat()
    mainCSP = state.mainCSP
    mainCSPChange = mainCSP != tempFloat
    mainCSP = tempFloat
	//heating set point
	tempFloat = thermostat.currentValue("heatingSetpoint").toFloat()
    def mainHSP = state.mainHSP
    def mainHSPChange = mainHSP != tempFloat
    mainHSP = tempFloat
    def mainOn = mainState != "idle"
    //always update state vars
    state.mainState = mainState
    state.mainMode = mainMode
    state.mainCSP = mainCSP
    state.mainHSP = mainHSP
    state.mainTemp = mainTemp
    
    //update cycle data
    if (mainStateChange && mainOn){
    	//main start
        state.startTime = now() + location.timeZone.rawOffset
        state.startTemp = mainTemp
        state.mainStatePrior = mainState
        def usageWatts = power?.currentValue("power") as double
        state.startWattage = usageWatts
        log.warn "start power = $state.startWattage"
        def wattage = usageWatts - 600 //state.idleUse
        if(state.mainStatePrior == "heat") state.avgConsH  = !state.avgConsH ?  wattage : state.avgConsH < wattage ? wattage : state.avgConsH
        if(state.mainStatePrior == "cool") state.avgConsC  = !state.avgConsC ?  wattage : state.avgConsC < wattage ? wattage : state.avgConsC
    } else if (mainStateChange && !mainOn){
    	//main end
        state.endTime = now() + location.timeZone.rawOffset
        state.endTemp = mainTemp
        state.idleUse = power?.currentValue("power") as double
        state.stopWattage = power?.currentValue("power") as double
        log.warn "stop power = $state.stopWattage"
        //update historical data
        if ((state.startTime && state.endTime) && (state.startTime < state.endTime)){
                rtm = ((state.endTime - state.startTime) / 60000).toInteger()
                if(currDate == today) {
                	log.info "recording daily data"
                    if(state.mainStatePrior == "heat"){
                        state.dailyUseH = state.dailyUseH ? state.dailyUseH + rtm : rtm //state.mainTemp ?: thermostat.currentValue("temperature").toFloat()
                    }
                    else if (state.mainStatePrior == "cool")  state.dailyUseC = state.dailyUseC ? state.dailyUseC + rtm : rtm
                }
                else {
                	//reset daily data
                    log.info "resetting daily data"
                    state.dailyUseH = 0
                    state.dailyUseC = 0
                }
                if(currMonth == thisMonth) {
                	log.info "recording monthly data"
                    if(state.mainStatePrior == "heat"){
                        state.monthlyUseH = state.monthlyUseH ? state.monthlyUseH + rtm : rtm
                    }
                    else if (state.mainStatePrior == "cool") state.monthlyUseC = state.monthlyUseC ? state.monthlyUseC + rtm : rtm 
                }
                else {
                	//reset monthly data
                    log.info "resetting monthly data"                
                    state.monthlyUseH = 0
                    state.monthlyUseC = 0
                }
        }
    }
    if (mainStateChange || mainModeChange || mainCSPChange || mainHSPChange){
    	def dataSet = [msg:"stat",data:[initRequest:false,mainState:mainState,mainStateChange:mainStateChange,mainMode:mainMode,mainModeChange:mainModeChange,mainCSP:mainCSP,mainCSPChange:mainCSPChange,mainHSP:mainHSP,mainHSPChange:mainHSPChange,mainOn:mainOn]]
        state.dataSet = dataSet
	}
}
def temperatureHandler(evt) {
    def currentTemp
    def eTxt
    def data = [:]
    if(modeOk && daysOk && timeOk && modeNotAwayOk)  {
        //Sending Data to WebCore
        data = [args: "Climate Control Profile executed"]
        sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")
        if (parent.debug) log.debug "sendNotificationEvent sent to CoRE from ${app.label}"
    
            if(sensor){            
                def sensors = sensor.size()
            	def tempAVG = sensor ? getAverage(sensor, "temperature") : "undefined device"
            	currentTemp = tempAVG
                //currentTemp = sensor.latestValue("temperature")
                if (debug) log.debug "Data check (avg temp: ${currentTemp}, num of sensors:${sensors}, app status: ${lastStatus})"
            }
            else {
            	currentTemp = thermostat.latestValue("temperature")
                 if (debug) log.debug "Thermostat data (curr temp: ${currentTemp},status: ${lastStatus}"
            }        
            if(setLow > setHigh){
                def temp = setLow
                	setLow = setHigh
                	setHigh = temp
                if(info) log.info "Detected ${setLow} >  ${setHigh}. Auto-adjusting setting to  ${temp}" 
            }         
            if (doorsOk) {
           		def currentMode = thermostat.latestValue("thermostatMode")
                def currentHSP = thermostat.latestValue("heatingSetpoint") 
                def currentCSP = thermostat.latestValue("coolingSetpoint") 	
                	if (debug) log.debug "App data (curr temp: ${currentTemp},curr mode: ${currentMode}, currentHSP: ${currentHSP},"+
                    						" currentCSP: ${currentCSP}, last status: ${lastStatus}"
                
                if (currentTemp < setLow) {
                    if (state.lastStatus == "one" || state.lastStatus == "two" || state.lastStatus == "three" || state.lastStatus == null){
                        state.lastStatus = "one" 
                         if (currentMode == "cool" || currentMode == "off") {
                     		def msg = "Adjusting ${thermostat} operating mode and setpoints because temperature is below ${setLow}"
                           	if (cold) thermostat?."${cold}"()
                           	thermostat?.setHeatingSetpoint(SetHeatingLow)
                           	if (SetCoolingLow) thermostat?.setCoolingSetpoint(SetCoolingLow)
                           	thermostat?.poll()
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)
                            if (info) log.info msg
							state.firstCheck = true
                        }
                     	else if  (currentHSP < SetHeatingLow) {
                            def msg = "Adjusting ${thermostat} setpoints because temperature is below ${setLow}"
                     		thermostat?.setHeatingSetpoint(SetHeatingLow)
                     		if (SetCoolingLow) thermostat?.setCoolingSetpoint(SetCoolingLow)
                            thermostat?.poll()
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)
                        		if (info) log.info msg
								state.firstCheck = true
                        }
                     	else if  (currentHSP >= SetHeatingLow && state.firstCheck == true) {
                            def msg = "Your room temperature ${thermostat} has reached $currentTemp, but your Heat is set to $currentHSP, you may consider turning up the heat to be more comfortable"
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)
                        		if (info) log.info msg
								state.firstCheck = false
                        }                        
                    }
                }                                     
                if (currentTemp > setHigh) {
                    if (state.lastStatus == "one" || state.lastStatus == "two" || state.lastStatus == "three" || state.lastStatus == null){
                        state.lastStatus = "two"
						if (currentMode == "heat" || currentMode == "off") {
                            def msg = "Adjusting ${thermostat} operating mode and setpoints because temperature is above ${setHigh}"
                        	if (hot) thermostat?."${hot}"()
                        	if (SetHeatingHigh) thermostat?.setHeatingSetpoint(SetHeatingHigh)
                        	thermostat?.setCoolingSetpoint(SetCoolingHigh)
                        	thermostat?.poll()
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)
                            	if (info) log.info msg
								state.firstCheck = true
                        }
                        else if (currentCSP > SetCoolingHigh) {
                            def msg = "Adjusting ${thermostat} setpoints because temperature is above ${setHigh}"
                    		thermostat?.setCoolingSetpoint(SetCoolingHigh)
                     		if (SetHeatingHigh) thermostat?.setHeatingSetpoint(SetHeatingHigh)
                            thermostat?.poll()
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)  
                            	if (info) log.info msg
								state.firstCheck = true
                       	}
                        else if (currentCSP <= SetCoolingHigh && state.firstCheck == true) {
                            def msg = "Your room temperature ${thermostat} has reached $currentTemp, but your AC is set to $currentCSP, you may consider turning the AC to be more comfortable"
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)  
                            	if (info) log.info msg
                                state.firstCheck = false
                       	}                        
                   }     
                }    
                if (currentTemp > setLow && currentTemp < setHigh) {
                        if (neutral == true) {
                            if (debug) log.debug "Neutral is ${neutral}, current temp is: ${currentTemp}"
                            if (state.lastStatus == "two" || state.lastStatus == "one" || state.lastStatus == null){
                                def msg = "Adjusting ${thermostat} mode to off because temperature is neutral"
                                    thermostat?.off()
                                    thermostat?.poll()
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)
                                    state.lastStatus = "three"
                                        if (info) log.info msg
                                        if (debug) log.debug "Data check neutral(neutral is:${neutral}, currTemp: ${currentTemp}, setLow: ${setLow}, setHigh: ${setHigh})"
                            }
                       }
                						if (info) log.info "Temperature is neutral not taking action because neutral mode is: ${neutral}"      
                }
            }
            else{
                def delay = (turnOffDelay != null && turnOffDelay != "") ? turnOffDelay * 60 : 60
               		if(info) log.info ("Detected open doors.  Checking door states again in ${delay} seconds")
                runIn(delay, "doorCheck")
            }
	}
    	if (debug) log.debug "Temperature handler called: modeOk = $modeOk, daysOk = $daysOk, timeOk = $timeOk, modeNotAwayOk = $modeNotAwayOk "
}

def modeAwayChange(evt){ 
	if(modeOk && daysOk && timeOk){
    	if (modes2){
            if(modes2.contains(location.mode)){
                    state.lastStatus = "away"
                    if (away) thermostat."${away}"()             
                    if(SetHeatingAway) thermostat.setHeatingSetpoint(SetHeatingAway)
                    if(SetCoolingAway) thermostat.setCoolingSetpoint(SetCoolingAway)
                    if(fanAway) thermostat.setThermostatFanMode(fanAway)
                    def msg = "Adjusting ${thermostat} mode and setpoints because Location Mode is set to Away"   
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg) 
                    if(info) log.info "Running AwayChange because mode is now ${away} and last staus is ${lastStatus}"
            }
            else  {
        			state.lastStatus = null
                    temperatureHandler()
                    if(info) log.info "Running Temperature Handler because Home Mode is no longer in away, and the last staus is ${lastStatus}"
			}
     	}
					if(info) log.info ("Detected temperature change while away but all settings are ok, not taking any actions.")
    }
}

def modeAwayTempHandler(evt) {
	def tempAVGaway = sensor ? getAverage(sensor, "temperature") : "undefined device"
	def currentAwayTemp = thermostat.latestValue("temperature")
	
    	if(info) log.info "Away: your average room temperature is:  ${tempAVGaway}, current temp is  ${currentAwayTemp}"
		if (sensor) currentAwayTemp = tempAVGaway		
        if(lastStatus == "away"){
        	if(modes2.contains(location.mode)){
           		if (currentAwayTemp < setAwayLow) {
					if(Awaycold) thermostat?."${Awaycold}"()
                    thermostat?.poll()
                    def msg = "I changed your ${thermostat} mode to ${Awaycold} because temperature is below ${setAwayLow}"
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)
                    	if (info) log.info msg
  				}
				if (currentAwayTemp > setHigh) {
					if(Awayhot) thermostat?."${Awayhot}"()
                    thermostat?.poll()
					def msg = "I changed your ${thermostat} mode to ${Awayhot} because temperature is above ${setAwayHigh}"
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)
                    	if (info) log.info msg
  				}
             }
			else {
        			state.lastStatus = null
            		temperatureHandler()
            		if(info) log.info "Temp changed while staus is ${lastStatus} but the Location Mode is no longer in away. Resetting lastStatus"
        	}
	}
}
def getNormalizedOS(os){
	def normOS = ""
    if (os == "heating" || os == "pending heat" || os == "heat" || os == "emergency heat"){
    	normOS = "heat"
    } else if (os == "cooling" || os == "pending cool" || os == "cool"){
    	normOS = "cool"
    } else if (os == "auto"){
    	normOS = "auto"
    } else if (os == "off"){
    	normOS = "off"
    } else {
    	normOS = "idle"
    }
    return normOS
}
def doorCheck(evt){	
        state.disabledTemp = sensor.latestValue("temperature")
       	state.disabledMode = thermostat.latestValue("thermostatMode")
       	state.disableHSP = thermostat.latestValue("heatingSetpoint") 
        state.disableCSP = thermostat.latestValue("coolingSetpoint") 
		if (debug) log.debug "Disable settings: ${state.disabledMode} mode, ${state.disableHSP} HSP, ${state.disableCSP} CSP"
    if (!doorsOk){
		if(info) log.info ("doors still open turning off ${thermostat}")
		def msg = "I changed your ${thermostat} mode to off because some doors are open"
        if (state.lastStatus != "off"){
        	thermostat?.off()
                           	if(recipients?.size()>0 || sms?.size()>0 || push) sendtxt(msg) //sendMessage(msg)
                         	if(speechSynth || sonos ) playMessage(msg)
            	if (info) log.info msg
		}
		state.lastStatus = "off"
        		if (info) log.info "Changing status to off"
	}
	else {
    	if (state.lastStatus == "off"){
			state.lastStatus = null
		    if (resetOff){
               if(debug) log.debug "Contact sensor(s) are now closed restoring ${thermostat} with settings: ${state.disabledMode} mode"+
               ", ${state.disableHSP} HSP, ${state.disableCSP} CSP"
                thermostat."${state.disabledMode}"()             
                thermostat.setHeatingSetpoint(state.disableHSP)
                thermostat.setCoolingSetpoint(state.disableCSP) 		    
	    	}
        }
        temperatureHandler()
        	if(debug) "Calling Temperature Handler"
	}
}

private getAverage(device, type){
	def total = 0
    device.each {total += it.latestValue("temperature")}
    return Math.round(total/device.size())
}

def getAverageUI(){
	def total = 0
    def currentTemp
            if(sensor){            
                def sensors = sensor.size()
            	def tempAVG = sensor ? getAverage(sensor, "temperature") : "undefined device"
            	currentTemp = tempAVG
			}
   return currentTemp
}

/***********************************************************************************************************************
    TAKE ACTIONS HANDLER
***********************************************************************************************************************/
private playMessage(eTxt) {
	def sVolume
    def sTxt = textToSpeech(eTxt instanceof List ? eTxt[0] : eTxt)
    int prevDuration
    if(state.sound) prevDuration = state.sound.duration as Integer
    if(sonosDelay)	prevDuration = prevDuration + sonosDelay 
	state.sound = sTxt
	state.lastPlayed = now()

    //Playing Audio Message
        if (speechSynth) {
            def currVolLevel = speechSynth.latestValue("level")
            def currMute = speechSynth.latestValue("mute")
                log.debug "vol switch = ${currVolSwitch}, vol level = ${currVolLevel}, currMute = ${currMute} "
                sVolume = settings.speechVolume ?: 30 
                speechSynth?.playTextAndResume(eTxt, sVolume)
                state.lastPlayed = now()
                log.info "Playing message on the speech synthesizer'${speechSynth}' at volume '${sVolume}'"
        }
        if (sonos) { 
            def currVolLevel = sonos.latestValue("level") //as Integer
            currVolLevel = currVolLevel[0]
            def currMuteOn = sonos.latestValue("mute").contains("muted")
                if (currMuteOn) { 
                    log.error "speaker is on mute, sending unmute command"
                    sonos.unmute()
                }
                sVolume = settings.sonosVolume ?: 20
                sVolume = (sVolume == 20 && currVolLevel == 0) ? sVolume : sVolume !=20 ? sVolume: currVolLevel
                def elapsed = now() - state.lastPlayed
                def elapsedSec = elapsed/1000
                //log.warn "previous duration = $prevDuration, elapsedSec = $elapsedSec "
                def timeCheck = prevDuration * 1000
                //log.warn "elapsed= $elapsed, timeCheck = $timeCheck"
                def sCommand = resumePlaying == true ? "playTrackAndResume" : "playTrackAndRestore"
                    if(elapsed < timeCheck){
                    	def delayNeeded = prevDuration - elapsedSec
                        if(delayNeeded > 0 ) delayNeeded = delayNeeded + 2
                        log.error "message is already playing, delaying new message by $delayNeeded seconds"
                        state.sound.command = sCommand
                        state.sound.volume = sVolume
                        state.lastPlayed = now()
                        runIn(delayNeeded , delayedMessage)
                	}
                    else {
                    	log.info "playing first message"
                		sonos?."${sCommand}"(sTxt.uri, Math.max((sTxt.duration as Integer),2), sVolume)
                        state.lastPlayed = now()
						state.sound.command = sCommand
                        state.sound.volume = sVolume
                	}
        }      
}
def delayedMessage() {
def sTxt = state.sound
sonos?."${sTxt.command}"(sTxt.uri, Math.max((sTxt.duration as Integer),3), sTxt.volume)
log.warn "delayed message is now playing"
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
    	if (push) {
        	message = timeStamp==true ? message + " at " + stamp : message
    		sendPush message
            	if (parent.debug) log.debug "Sending push message to selected reipients"
        }
    } 
    if (notify) {
        sendNotificationEvent(message)
             	if (parent.debug) log.debug "Sending notification to mobile app"
    }
    if (sms) {
        sendText(sms, message)
	}
}
private void sendText(number, message) {
    if (sms) {
        def phones = sms.split("\\,")
        for (phone in phones) {
            sendSms(phone, message)
            if (parent.debug) log.debug "Sending sms to selected phones"
        }
    }
}

private getAllOk() {
	modeOk && daysOk && timeOk && doorsOk && modeNotAwayOk
}

private getModeOk() {
	def result = !modes || modes.contains(location.mode)
		if(debug) log.debug "modeOk = $result"
	result
}

private getModeNotAwayOk() {
	def result = !modes2 || !modes2.contains(location.mode)
		if(debug) log.debug "modeNotAwayOk = $result"
	result
}

private getDoorsOk() {
	def result = !doors || !doors.latestValue("contact").contains("open")
		if(debug) log.debug "doorsOk = $result"
	result
}
private getDaysOk() {
	def result = true
	if (days) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = days.contains(day)
	}
	if(debug) log.debug "daysOk = $result"
	result
}

private getTimeOk() {
	def result = true
	if (starting && ending) {
		def currTime = now()
		def start = timeToday(starting).time
		def stop = timeToday(ending).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
    
    else if (starting){
    	result = currTime >= start
    }
    else if (ending){
    	result = currTime <= stop
    }
    
		if(debug) log.debug "timeOk = $result"
	result
}

def getTimeLabel(starting, ending){

	def timeLabel = "Tap to set"
	
    if(starting && ending){
    	timeLabel = "Between" + " " + hhmm(starting) + " "  + "and" + " " +  hhmm(ending)
    }
    else if (starting) {
		timeLabel = "Start at" + " " + hhmm(starting)
    }
    else if(ending){
    timeLabel = "End at" + hhmm(ending)
    }
	timeLabel
}

private hhmm(time, fmt = "h:mm a")
{
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}
/************************************************************************************************************
	~ REPORTING ~
************************************************************************************************************/       
def getReport(rptName){
    //def t = tempSensors.currentValue("temperature")
    def reports = ""
    def cspStr = ""
    def tempAVG
    if (sensor) {
		tempAVG = getAverageUI()
	}
	def temp1 = (thermostat?.currentValue("temperature"))
	def setPC1 = (thermostat?.currentValue("coolingSetpoint"))
	def setPH1 = (thermostat?.currentValue("heatingSetpoint"))
	def mode1 = (thermostat?.currentValue("thermostatMode"))
	def oper1 = (thermostat?.currentValue("thermostatOperatingState"))
    
    if (rptName == "Current state"){
        def averageTemp = 0  
		reports =  "Main system:\n\tcurrent state: ${oper1}\n\tmode: ${mode1}\n\tcurrent thermostat temp: ${temp1}\n\theating set point: ${setPH1}\n\tcooling set point: ${setPC1}" + 
        			"\n\tsensor temperature (average): ${tempAVG}"
    }
    if (rptName == "General Settings"){
        reports = "Main system (current data):\n\tstate: ${state.mainState}\n\tmode: ${state.mainMode}\n\tthermostat temp: ${temp1}" +
        			"\n\theating set point: ${tempStr(state.mainHSP)}\n\tcooling set point: ${tempStr(state.mainCSP)}\n\tsensor temperature (average): ${tempAVG}"
        reports = reports + "\n\nSettings:\n\tmin temperature: ${setLow}\n\tmax temperature: ${setHigh}"
         if (away) reports = reports + "\n\taway mode: ${modes2[0]}.\n\n"
        if (doors) reports = reports + "\n\tdisable mode sensor(s): ${doors}\n\tdelay: ${turnOffDelay} minutes.\n\n"
    }  
    if (rptName == "Last results"){
        def stime = "No data available yet"
        def etime = "No data available yet"
        def sTemp = tempStr(state.startTemp)
        def eTemp  = tempStr(state.endTemp)
        def rtm = "No data available yet"
        if ((state.startTime && state.endTime) && (state.startTime < state.endTime)){
        	stime = new Date(state.startTime).format("yyyy-MM-dd HH:mm")
            etime =  new Date(state.endTime).format("yyyy-MM-dd HH:mm")
            rtm = ((state.endTime - state.startTime) / 60000).toInteger()
            rtm = "${rtm} minutes"
        }
        reports = "Main system:\n\tstart: ${stime}\n\tend: ${etime}\n\tstart temp: ${sTemp}\n\tend temp: ${eTemp}\n\toperating state: ${state.mainStatePrior}\n\tduration: ${rtm}\n\n"
    }
    if (rptName == "Historical results"){
        def dailyH = "N/A"
        def monthlyH = "N/A"
        def dailyC = "N/A"
        def monthlyC = "N/A"
        def powerConsumpH = "N/A"
        def powerConsumpC = "N/A"
        def ccost = 0
        def hcost = 0
        if (state.dailyUseH > 0 && state.monthlyUseH > 0){
			dailyH = state.dailyUseH < 60 ? state.dailyUseH + " minutes" : state.dailyUseH/60 + " hours"
         	monthlyH = state.monthlyUseH < 60 ? state.monthlyUseH + " minutes" : state.monthlyUseH/60 + " hours"
        	if(state.monthlyUseH) {
            	powerConsumpH = state.avgConsH ? ((state.avgConsH/1000) * (state.monthlyUseH/60)) : "N/A"
        		if(powerConsumpH != "N/A")  hcost = powerConsumpH * cost
        		log.warn "heating data: avgConsH = state.avgConsH , monthlyUseH = state.monthlyUseH, powerConsumpH = $powerConsumpH,  hcost = $hcost"
        	}
        }
        if (state.dailyUseC > 0 && state.monthlyUseC > 0){
			dailyC = state.dailyUseC < 60 ? state.dailyUseC + " minutes" : state.dailyUseC/60 + " hours"
         	monthlyC = state.monthlyUseC < 60 ? state.monthlyUseC + " minutes" : state.monthlyUseC/60 + " hours"
            
        	if(state.monthlyUseC) {
            	powerConsumpC = state.avgConsC ? ((state.avgConsC/1000) * (state.monthlyUseC/60)) : "N/A"
            	if(powerConsumpC != "N/A") ccost = powerConsumpC * cost
        		log.warn "cooling data: avgConsC = $state.avgConsC , monthlyUseC = $state.monthlyUseC, powerConsumpC = $powerConsumpC,  ccost = $ccost"
            }
        }
        if (state.dailyUseC > 0 && state.monthlyUseC > 0){
			dailyC = state.dailyUseC < 60 ? state.dailyUseC + " minutes" : state.dailyUseC/60 + " hours"
         	monthlyC = state.monthlyUseC < 60 ? state.monthlyUseC + " minutes" : state.monthlyUseC/60 + " hours"
        }		
        
        reports = "Main system:\n\tCooling (operating time):\n\t\tToday: ${dailyC}\n\t\tThis month: ${monthlyC}\n\t\tkwh: ${powerConsumpC} \n\t\tCost this Month: ${ccost} \n\n"
        reports = reports + "\tHeating (operating time):\n\t\tToday: ${dailyH}\n\t\tThis month: ${monthlyH}\n\t\tkwh: ${powerConsumpH} \n\t\tCost this Month: ${hcost}"
    }    

		return reports
}

def tempStr(temp){
    def tc = state.tempScale ?: location.temperatureScale
    if (temp != 0 && temp != null) return "${temp.toString()}°${tc}"
    else return "No data available yet."
}
def getAlexaReport() {
    def result = ""
    if (thermostat) {
    def disable = doors ? "active" : "not active" 
        text = "The ambiance mode for ${thermostat} is to adjust the thermostat if the temperature falls below ${setLow} or raises above ${setHigh}. "+
        		"The disable mode is ${disable} and the away mode is set when thermostat to away mode when Location Mode changes to: ${away}."
    }
    return result
}

def getStatusReport() {
    def result = ""
    if (thermostat) {
    def disable = doors ? "active" : "not active" 
        text = "The ambiance mode for ${thermostat} is to adjust the thermostat if the temperature falls below ${setLow} or raises above ${setHigh}. "+
        		"The disable mode is ${disable} and the away mode is set when thermostat to away mode when Location Mode changes to: ${away}."
    }
    return result
}

def greyedOut(){
    //state.var ? "complete": ""   
    def result = ""
    if (thermostat) {
    	result = "complete"	
    }
    result
}


def TemperatureSettingsParams() {
    def text = "Tap here to configure settings"
    if (thermostat) {
        text = "Current settings: adjust thermostat if temperature falls below ${setLow} or raises above ${setHigh}. Tap here to change settings"
    }
    text
}


def greyedOutDoors(){
	def result = ""
    if (doors) {
    	result = "complete"	
    }
    result
}

def ThermostatandDoorsParams() {
    def text = "Tap here to configure settings"
    if (doors) {
        text = "Current settings: thermostat turns off when ${doors} are open for more than ${turnOffDelay} minutes. Tap here to change settings"
    }
    text
}
def greyedOutAway(){
	def result = ""
    if (away) {
    	result = "complete"	
    }
    result
}
def ThermostatAwayParams() {
    def text = "Tap here to configure settings"
    if (away) {
        text = "Current settings: adjust thermostat to away mode when Location Mode is set to: ${modes2}. Tap here to change settings"
    }
    text
}
def reportingParam() {
    def text = "Tap here to configure settings"
    if (away) {
        text = "Current settings: adjust thermostat to away mode when Location Mode is set to: ${modes2}. Tap here to change settings"
    }
    text
}


def greyedOutSettings(){
	def result = ""
    if (starting || ending || days || modes || push || speechSynth || sonos) {
    	result = "complete"	
    }
    result
}

def SettingsParams() {
    def text = "Tap here to configure settings"
    if (starting || ending || days || modes || push || speechSynth || sonos) {
        text = "Other Settings have been configured. Tap here to change settings"
    }
    text
}


def greyedOutTime(starting, ending){
	def result = ""
    if (starting || ending) {
    	result = "complete"	
    }
    result
}

private anyoneIsHome() {
  def result = false
  if(people.findAll { it?.currentPresence == "present" }) {
    result = true
  }

	 if(debug) log.debug("anyoneIsHome: ${result}")

  return result
}


page(name: "timeIntervalInput", title: "Only during a certain time", refreshAfterSelection:true) {
		section {
			input "starting", "time", title: "Starting (both are required)", required: false 
			input "ending", "time", title: "Ending (both are required)", required: false 
		}
}

def pSendSettings() {def result = ""
    if (sendContactText || sendText || push) {
    	result = "complete"}
   		result}

def pSendComplete() {def text = "Tap here to configure settings" 
    if (sendContactText || sendText || push) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}
        
/******************************************************************************************************
   PARENT STATUS CHECKS
******************************************************************************************************/
def checkRelease() {
return state.NotificationRelease
}