/* 
* Profile Shortcuts - EchoSistant Add-on 
*
*		6/12/2017		Version:5.0 R.0.0.1		Alpha Release
*		2/17/2017		Version:4.0 R.0.0.1		Public Release
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
    name            : "Shortcuts",
    namespace        : "Echo",
    author            : "JH/BD",
    description        : "EchoSistant Profile Shortcuts Add-on: Create Action Shortcuts for your Profiles\nDo NOT Install From the Marketplace\nPlease Use the Echosistant5 SmartApp and Create a Profile first",
    category        : "My Apps",
    parent            : "Echo:Profiles",
    iconUrl            : "https://raw.githubusercontent.com/tonesto7/app-icons/master/es5_shortcuts.png",
    iconX2Url        : "https://raw.githubusercontent.com/tonesto7/app-icons/master/es5_shortcuts.png",
    iconX3Url        : "https://raw.githubusercontent.com/tonesto7/app-icons/master/es5_shortcuts.png")
/**********************************************************************************************************************************************/
private profVersion() { return "5.0.0" }
private release() { return "R.0.4.0" }
private appVerDate() { "10-27-2017" }
private moduleType() { return "shortcuts" }
preferences {
    page name: "mainProfilePage"
    page name: "sDevices"
}

/******************************************************************************
    MAIN PROFILE PAGE
******************************************************************************/
def mainProfilePage() {
    dynamicPage(name: "mainProfilePage", title:"", install: true, uninstall: installed) {
        section("Echosistant Shortcut Info:") {
            def str = "Shortcut Version: V${release()}"
            str += "\nModified: ${appVerDate()}"
            paragraph str, state: "complete", image: getAppImgTony("es5_shortcuts.png")
        }
        section ("Name Your Shortcut") {
            //paragraph title: "NOTICE", "This is the phrase that will be used to match this shortcut to the command."
            //paragraph title: "NOTE", "When you say this phrase to Alexa..."
            input "shortcutName", "text", title: "When you say this to Alexa...", submitOnChange: true, required:true, image: getAppImgTony("es5_prf_name.png")
        	paragraph "If this shortcut is a 'Main Intent' shortcut, it can ONLY be triggered by a SHORTCUT TRIGGER"
        }
        section ("Alexa's Response") {
        	if(scTrigger) {
            	paragraph "Alexa will only respond if this shortcut is triggered verbally, NOT from the shortcut trigger"
                }
            input "scResponse", "text", title: "...she will respond with this.", required: false, submitOnChange: true
        }
        section ("Alexa Routines Trigger") {
        	paragraph "You can also trigger this shortcut when this switch activates in a 'Native Alexa Routine'"
            input "scTrigger", "capability.switch", title: "Shortcut Trigger Switch", multiple: true, submitOnChange: true, required:false, image: getAppImgTony("es5_prf_name.png")
            if (settings?.scTrigger) {
                input "scTriggerCmd", "enum", title: "Trigger when this switch turns...", options:["on":"On","off":"Off"], multiple: false, required: false, submitOnChange:true, image: getAppImgTony("command.png")
            }
		}
        // section ("...OR Trigger These Actions" ) {
        //     //paragraph title: "NOTICE", "This is the phrase that will be used to match this shortcut to the command."
        //     input "advShortcut", "bool", title: "Enable Advanced Shortcuts", required:false, submitOnChange: true, image: getAppImgTony("es5_prf_name.png")
        // }
        // section ("Alexa should take this action...") {
        //     input "qActionName", "text", title: "Quick Action Name", submitOnChange: true, required:false, image: getAppImgTony("es5_prf_name.png")
        // }

        // if(advShortcut==true){
            section ("Alexa will also perform these device actions") {
                href "sDevices", title: "Select Devices...", description: pDevicesComplete(), state: pDevicesSettings()  , image: getAppImgTony("devices.png") //state: (pDevicesComplete() ? "complete": "")
            }
            section("...as well as changing the mode") {  // Changed by Jason - Modes were not working
                // I really don't like to use the names of the Modes.  Because if a user changes the name it breaks all references to it.  I find it more reliable to use the modes ID
                //location?.modes?.sort{it?.name}?.collect { [(it?.id):it?.name] }
                def modes = location.modes.name.sort()
                input "pMode", "enum", title: "Choose Mode to change to...", options: modes, multiple: false, required: false , image: getAppImgTony("mode.png")
            }
            section("...executing routines") {
                def routines = location.helloHome?.getPhrases()?.sort {it?.label }?.collect { [(it?.id):it?.label] }
                if (routines) {
                    input "pRoutine", "enum", title: "Run a Routine", required: false, options: routines, multiple: false, submitOnChange: true, image: getAppImgTony("routines.png")
                    if (settings?.pRoutine) {
                        input "pRoutine2", "enum", title: "Execute a Second Routine...", required: false, options: routines, multiple: false, submitOnChange: true, image: getAppImgTony("routines.png")
                    }
                }
            }

section("...and SHM Management") {
                input "shmState", "enum", title: "Smart Home Alarm Mode", options:["stay":"Armed Stay","away":"Armed Away","off":"Disarmed"], multiple: false, required: false,
                        submitOnChange: true, image: getAppImgTony("home_security.png")
                if (settings?.shmState) {
                    input "shmStateKeypads", "capability.lockCodes",  title: "Send SHM Status to these Keypads...", multiple: true, required: false, submitOnChange: true,
                            image: getAppImgTony("number_keypad.png")
                }
            }
        // }
        // section ("Name Your Shortcut") {
        //     label title:"Shortcut Name", required: false, submitOnChange: true, image: getAppImgTony("es5_prf_name.png")
        // }
    }
}

/******************************************************************************
    DEVICES SELECTION PAGE
******************************************************************************/
page name: "sDevices"
def sDevices() {
    dynamicPage(name: "sDevices", title: "",install: false, uninstall: false) {
        section ("Lights/Switches"){
            input "sSwitches", "capability.switch", title: "Lights | Switches", multiple: true, required: false, submitOnChange: true, image: getAppImgTony("wall_switch.png")
            if (settings?.sSwitches) {
                input "sSwitchesCmd", "enum", title: "Switch Command", options:["on":"Turn on","off":"Turn off","toggle":"Toggle"], multiple: false, required: false, submitOnChange:true, image: getAppImgTony("command.png")
            }
        }
        if (settings?.sSwitchesCmd) {
            section("More Lights/Switches") {
                input "sOtherSwitch", "capability.switch", title: "Other Lights | Switches", multiple: true, required: false, submitOnChange: true, image: getAppImgTony("wall_switch.png")
                if (settings?.sOtherSwitch) {
                    input "sOtherSwitchCmd", "enum", title: "Switch Command", options: ["on1":"Turn on","off1":"Turn off","toggle":"Toggle"], multiple: false, required: false,
                            submitOnChange: true, image: getAppImgTony("command.png")
                }
            }
        }
        section ("Dimmers"){
            input "sDimmers", "capability.switchLevel", title: "Dimmable Lights", multiple: true, required: false , submitOnChange:true, image: getAppImgTony("speed_knob.png")
            if (settings?.sDimmers) {
                input "sDimmersCmd", "enum", title: "Dimmer Command", options:["on":"Turn on","off":"Turn off","set":"Set level","decrease":"Dim by %","increase":"Brighten by %"], multiple: false, required: false, submitOnChange:true, image: getAppImgTony("command.png")
                if (sDimmersCmd=="decrease") {
                    input "sDimDecrease", "number", title: "Dim the lights by this %", required: false, submitOnChange: true
                }
                if (sDimmersCmd == "increase") {
                    input "sDimIncrease", "number", title: "Brighten the lights by this %", required: false, submitOnChange: true
                }
                if (settings?.sDimmersCmd == "set") {
                    input "sDimmersLVL", "number", title: "Dimmer Level", description: "Set Dimmer Level", range: "0..100", required: false, submitOnChange: true
                 }
            }
        }
        if (settings?.sDimmersCmd) {
            section("More Dimmers") {
                input "sOtherDimmers", "capability.switchLevel", title: "Other Dimmable Lights", multiple: true, required: false , submitOnChange:true, image: getAppImgTony("speed_knob.png")
                if (settings?.sOtherDimmers) {
                    input "sOtherDimmersCmd", "enum", title: "Other Dimmer Command", options:["on":"Turn on","off":"Turn off","set":"Set level","decrease":"Dim by %","increase":"Brighten by %"], multiple: false, required: false, submitOnChange:true,
                              image: getAppImgTony("command.png")
                    if (sOtherDimmersCmd=="decrease") {
                        input "sOtherDimDecrease", "number", title: "Dim the lights by this %", required: false, submitOnChange: true
                    }
                    if (sOtherDimmersCmd == "increase") {
                        input "sOtherDimIncrease", "number", title: "Brighten the lights by this %", required: false, submitOnChange: true
                     }
                    if (settings?.sOtherDimmersCmd == "set") {
                        input "sOtherDimmersLVL", "number", title: "Dimmer Level", description: "Set Level...", range: "0..100", required: false, submitOnChange: true
                    }
                }
            }
        }
        section ("Color Lights"){
            input "sHues", "capability.colorControl", title: "Color Lights", multiple: true, required: false, submitOnChange:true, image: getAppImgTony("color_wheel.png")
            if (settings?.sHues) {
                input "sHuesCmd", "enum", title: "Color Light Command?", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false,
                        submitOnChange:true, image: getAppImgTony("command.png")
                if(settings?.sHuesCmd == "setColor") {
                    input "sHuesColor", "enum", title: "Select Color?", required: false, multiple:false, options: fillColorSettings()?.name
                }
//                if(settings?.sHuesCmd == "setColor" || settings?.sHuesCmd == "on") {
//                    input "sHuesLevel", "enum", title: "Light Level?", required: false, range: "0..100", options: getSetLevelEnum(), submitOnChange:true
//                }
            }
        }
        if (settings?.sHuesLevel) {
            section("More Color Lights") {
                input "sHuesOther", "capability.colorControl", title: "Other Color Lights", multiple: true, required: false, submitOnChange:true, image: getAppImgTony("color_wheel.png")
                if (settings?.sHuesOther) {
                    input "sHuesOtherCmd", "enum", title: "Other Color Lights Command", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false,
                            submitOnChange:true, image: getAppImgTony("command.png")
                    if(settings?.sHuesOtherCmd == "setColor") {
                        input "sHuesOtherColor", "enum", title: "Select Color?", required: false, multiple:false, options: fillColorSettings()?.name
                    }
//                    if(settings?.sHuesOtherCmd == "on" || settings?.sHuesOtherCmd == "setColor") {
//                        input "sHuesOtherLevel", "enum", title: "Light Level?", required: false, range: "0..100", image: getAppImgTony("speed_knob.png"), options: getSetLevelEnum()
//                    }
                }
            }
        }
        section ("Momentary Switches"){
            input "mSwitches", "capability.switch", title: "Momentary Devices", multiple: true, required: false, submitOnChange: true, image: getAppImgTony("push_button.png")
            if (settings?.mSwitches) {
                input "mOff", "number", title: "Turn Off in (Seconds)", required: true, defaultValue: 3, submitOnChange: true
            }
        }
        section ("Flash These Switches") {
            input "sFlash", "capability.switch", title: "Flash These Switch(es)", multiple: true, required: false, submitOnChange:true, image: getAppImgTony("flashing_light.png")
            if (settings?.sFlash) {
                input "numFlashes", "number", title: "This number of times (default 3)", required: false, submitOnChange:true
                input "onFor", "number", title: "On for (default 1 second)", required: false, submitOnChange:true
                input "offFor", "number", title: "Off for (default 1 second)", required: false, submitOnChange:true
            }
        }
        section ("Control Fans") {
            input "sFans", "capability.switchLevel", title: "Ceiling Fans and Fans", multiple: true, required: false, submitOnChange: true, image: getAppImgTony("home_fan.png")
            if (settings?.sFans) {
                input "sFansCmd", "enum", title: "Fan Command?", options:["on":"Turn on","off":"Turn off","low":"Low","med":"Med","high":"High","incr":"Speed Up","decr":"Slow Down"], multiple: false, required: false, submitOnChange:true,
                        image: getAppImgTony("command.png")
                if (sFansCmd == "incr") {
                    input "sFanIncr", "number", title: "Increase the fan by this %", required: true, submitOnChange: true
                }
                if (sFansCmd == "decr") {
                    input "sFanDecr", "number", title: "Decrease the fan by this %", required: true, submitOnChange: true
                 }
            }
        }
        section ("Locks", hideWhenEmpty: true){
            input "sLocks", "capability.lock", title: "Locks", multiple: true, required: false, submitOnChange: true, image: getAppImgTony("lock.png")
            if (settings?.sLocks) {
                input "locksCmd", "enum", title: "Lock Command?", options:["lock":"Lock","unlock":"Unlock"], multiple: false, required: false, submitOnChange:true, image: getAppImgTony("command.png")
            }
        }
        section ("Garage Doors", hideWhenEmpty: true) {
            input "sDoor", "capability.garageDoorControl", title: "Garage Doors", multiple: true, required: false, submitOnChange: true, image: getAppImgTony("garage_door.png")
            if (settings?.sDoor) {
                input "sDoorCmd", "enum", title: "Garage Door Command", options:["open":"Open","close":"Close"], multiple: false, required: false, submitOnChange:true, image: getAppImgTony("command.png")
            }
        }
        section ("Thermostats") {
            input "cTstat", "capability.thermostat", title: "Thermostat(s)", multiple: true, required: false, submitOnChange:true, image: getAppImgTony("thermostat2.png")
            if (settings?.cTstat) {
                input "cTstatFan", "enum", title: "Fan Mode", options:["auto":"Auto","on":"On","off":"Off","circ":"Circulate"], multiple: false, required: false, submitOnChange:true,
                        image: getAppImgTony("tstat_fan.png")
                input "cTstatMode", "enum", title: "Operating Mode", options:["cool":"Cool","heat":"Heat","auto":"Auto","on":"On","off":"Off","incr":"Increase","decr":"Decrease"], multiple: false, required: false, submitOnChange:true,
                        image: getAppImgTony("mode.png")
                if (settings?.cTstatMode in ["cool","auto"]) { input "coolLvl", "number", title: "Cool Setpoint", required: true, submitOnChange: true, image: getAppImgTony("cool_icon.png") }
                if (settings?.cTstatMode in ["heat","auto"]) { input "heatLvl", "number", title: "Heat Setpoint", required: true, submitOnChange: true, image: getAppImgTony("heat_icon.png") }
                if (settings?.cTstatMode in ["incr","decr"]) {
                if (cTstatMode == "decr") {paragraph "NOTE: This will decrease the temp from the current room temp minus what you choose."}
                if (cTstatMode == "incr") {paragraph "NOTE: This will increase the temp from the current room temp plus what you choose."}
                input "tempChange", "number", title: "By this amount...", required: true, submitOnChange: true }
            }
		}
		if(settings?.cTstat) {
			section("More Thermostats") {
        		input "cTstat1", "capability.thermostat", title: "Additional Thermostat(s)...", multiple: true, required: false, submitOnChange:true, image: getAppImgTony("thermostat.png")
            	if (settings?.cTstat1) {
                	input "cTstat1Fan", "enum", title: "Fan Mode", options:["auto":"Auto","on":"On","off":"Off","circ":"Circulate"],multiple: false, required: false, submitOnChange:true,
							image: getAppImgTony("tstat_fan.png")
                	input "cTstat1Mode", "enum", title: "Operating Mode", options:["cool":"Cool","heat":"Heat","auto":"Auto","on":"On","off":"Off","incr":"Increase","decr":"Decrease"],multiple: false, required: false, submitOnChange:true,
							image: getAppImgTony("mode.png")
                    if (settings?.cTstat1Mode in ["cool","auto"]) { input "coolLvl1", "number", title: "Cool Setpoint", required: true, submitOnChange: true, image: getAppImgTony("cool_icon.png") }
                    if (settings?.cTstat1Mode in ["heat","auto"]) { input "heatLvl1", "number", title: "Heat Setpoint", required: true, submitOnChange: true, image: getAppImgTony("heat_icon.png") }
				if (settings?.cTstat1Mode in ["incr","decr"]) {
                if (cTstat1Mode == "decr") {paragraph "NOTE: This will decrease the temp from the current room temp minus what you choose."}
                if (cTstat1Mode == "incr") {paragraph "NOTE: This will increase the temp from the current room temp plus what you choose."}
                input "tempChange1", "number", title: "By this amount...", required: true, submitOnChange: true }
				}
            }
        }
        section ("Vents", hideWhenEmpty: true){
            input "sVents", "capability.switchLevel", title: "Vent(s)", multiple: true, required: false, submitOnChange: true, image: getAppImgTony("vent.png")
            if (settings?.sVents) {
                input "ventsCmd", "enum", title: "Vent Command",
                options:["on":"Open","off":"Close","25":"25%","50":"50%","75":"75%"], multiple: false, required: false, submitOnChange:true, image: getAppImgTony("command.png")
            }
        }
        section ("Shades", hideWhenEmpty: true){
            input "sShades", "capability.windowShade", title: "Window Shades | Coverings", multiple: true, required: false, submitOnChange: true, image: getAppImgTony("window_shade.png")
            if (settings?.sShades) {
                input "shadesCmd", "enum", title: "Shade Commands", options:["on":"Open","off":"Close","25":"25%","50":"50%","75":"75%"], multiple: false, required: false,
                        submitOnChange:true, image: getAppImgTony("command.png")
            }
        }
        section ("Presence Sensors", hideWhenEmpty: true){
            input "presence", "capability.presenceSensor", title: "Presence Sensor", multiple: true, required: false, submitOnChange: true, image: getAppImgTony("recipient.png")
            if (settings?.presence) {
                input "presenceCmd", "enum", title: "Set this person to...", options:["present":"Present","departed":"Not Present"], multiple: false, required: false, submitOnChange:true,
                        image: getAppImgTony("command.png")
            }
        }
    }
}

/************************************************************************************************************
Base Process
************************************************************************************************************/
def installed() {
    // log.debug "Installed with settings: ${settings}, current app version: ${release()}"
    atomicState?.isInstalled = true
    initialize()
}

def updated() {
    // log.debug "Updated with settings: ${settings}, current app version: ${release()}"
    unsubscribe()
    initialize()
}

def initialize() {
    //SHM status change and keypad initialize
    def label = "${fmtLabel(settings?.shortcutName)}"
    if(app?.label.toString() != label) { app?.updateLabel(label) }
//    atomicState?.roomName = parent?.getRoomName() as String
    if(scTrigger) {
    //	subscribe(scTrigger, "switch.on", processVerbal)
    	subscribe(scTrigger, "switch.on", processActions)
    	}
    // subscribe(location, locationHandler)
}

def getSetLevelEnum() {
    return [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]]
}

def getShortcutName() {
    return settings?.shortcutName as String
}

def fmtLabel(lbl) {
    return lbl?.capitalize()?.trim()
}

def getQActionName() {
    return settings?.qActionName as String
}

def getStModeById(mId) {
    return location?.getModes()?.find{it?.id == mId}
}

def getRoutineById(rId) {
    return location?.helloHome?.getPhrases()?.find{it?.id == rId}
}

// PROFILE ACTIONS
def devicesSelected() {
    if (sDoor || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers ||
        sHues || sHuesOther || sFlash || sFans || sShades || sVents || sLocks ||
        thermostats || thermostats1 || mSwitches || presence) {
        return true
    }
    return false
}

def gitRepo()                { return "BamaRayne/Echosistant"}
def gitBranch()                { return "master" }
def getWikiPageUrl()        { return "http://thingsthataresmart.wiki/index.php?title=EchoSistant" }
def getAppImg(imgName)         { return "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/$imgName" }
def getAppImgTony(imgName)    { return "https://raw.githubusercontent.com/tonesto7/app-icons/${gitBranch()}/$imgName" }
/******************************************************************************
    SHORTCUT HANDLERS
******************************************************************************/
//def processVerbal() {
//	processActions()
//    }
def processActions(skipDevs, testMode=false){
    log.info "the grandchild shortcut - $app.label, has executed"
    def logOut = []
    def error = false
    logOut << ["type":"trace", "text":"the grandchild shortcut - $app.label, has executed"]
    try {
        if(!testMode) {
            if (sSwitches || sOtherSwitch) {
                if (sSwitchesCmd == "on") {sSwitches.on()}
                if (sSwitchesCmd == "off") {sSwitches.off()}
                if (sSwitchesCmd == "toggle") {toggle1()}
                if (sOtherSwitchCmd == "on") {sOtherSwitch.on()}
                if (sOtherSwitchCmd == "off") {sOtherSwitch.off()}
                if (sOtherSwitchCmd == "toggle") {toggle2()}
            }
            if (sHues) { processColor()}
            if (sDimmers) {
                if (sDimmersCmd == "on") {sDimmers.on()}
                else if (sDimmersCmd == "off") {sDimmers.off()}
                if (sDimmersCmd == "set" && sDimmers) {
                    def level = sDimmersLVL < 0 || !sDimmersLVL ?  0 : sDimmersLVL >100 ? 100 : sDimmersLVL as int
                    sDimmers.setLevel(level)
                }
                if (sDimmersCmd == "increase" && sDimmers) {
                    def newLevel
                    sDimmers?.each {deviceD ->
                        def currLevel = deviceD.latestValue("level")
                        newLevel = sDimIncrease
                        newLevel = newLevel + currLevel
                        newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                        deviceD.setLevel(newLevel)
                    }
                }
                if (sDimmersCmd == "decrease" && sDimmers) {
                    def newLevel
                    sDimmers?.each {deviceD ->
                        def currLevel = deviceD.latestValue("level")
                        newLevel = sDimDecrease
                        newLevel = currLevel - newLevel
                        newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                        deviceD.setLevel(newLevel)
                    }
                }
            }
            if (sOtherDimmers) {
                if (sOtherDimmersCmd == "on") {sOtherDimmers.on()}
                else if (sOtherDimmersCmd == "off") {sOtherDimmers.off()}
                if (sOtherDimmersCmd == "set" && sOtherDimmers) {
                    def otherLevel = sOtherDimmersLVL < 0 || !sOtherDimmersLVL ?  0 : sOtherDimmersLVL >100 ? 100 : sOtherDimmersLVL as int
                    sOtherDimmers?.setLevel(otherLevel)
                }
                if (sOtherDimmersCmd == "increase" && sOtherDimmers) {
                    def newLevel
                    sOtherDimmers?.each { deviceD ->
                        def currLevel = deviceD.latestValue("level")
                        newLevel = sOtherDimIncrease
                        newLevel = newLevel + currLevel
                        newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                        deviceD.setLevel(newLevel)
                    }
                }
                if (sOtherDimmersCmd == "decrease" && sOtherDimmers) {
                    def newLevel
                    sOtherDimmersCmd?.each { deviceD ->
                        def currLevel = deviceD.latestValue("level")
                        newLevel = sOtherDimDecrease
                        newLevel = currLevel - newLevel
                        newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                        deviceD.setLevel(newLevel)
                    }
                }
            }
            if (sDoor) {
                if ( sDoorCmd == "open") {sDoor.open() }
                else { sDoor.close() }
            }
            if (sLocks) {
                if (sLocksCmd == "lock") { sLocks.lock() }
                else { sLocks.unlock() }
            }
            if (mSwitches) { momentaryDeviceHandler() }
            if (presence) {
                if (presenceCmd == "arrived") { presence.arrived() }
                else if (presenceCmd == "departed") {presence.departed() }
            }
            if (sFans) {
                if (sFansCmd == "on") {sFans.on()}
            	else if (sFansCmd == "off") {sFans.off()}
                else if (sFansCmd == "low") {sFans.setLevel(33)}
                else if (sFansCmd == "med") {sFans.setLevel(66)}
                else if (sFansCmd == "high") {sFans.setLevel(99)}
                if (sFansCmd == "incr" && sFans) {
                    def newLevel
                    sFans?.each {deviceD ->
                        def currLevel = deviceD.latestValue("level")
                        newLevel = sFanIncr
                        newLevel = newLevel + currLevel
                        newLevel = newLevel < 0 ? 0 : newLevel > 99 ? 99 : newLevel
                        deviceD.setLevel(newLevel)
                    }
                }
                if (sFansCmd == "decr" && sFans) {
                    def newLevel
                    sFans?.each {deviceD ->
                        def currLevel = deviceD.latestValue("level")
                        newLevel = sFanDecr
                        newLevel = currLevel - newLevel
                        newLevel = newLevel < 0 ? 0 : newLevel > 99 ? 99 : newLevel
                        deviceD.setLevel(newLevel)
                    }
                }
            }
            if (sVents) {
                if (sVentsCmd == "on") {sVents.setLevel(100)}
                else if (sVentsCmd == "off") {sVents.off()}
                else if (sVentsCmd == "25") {sVents.setLevel(25)}
                else if (sVentsCmd == "50") {sVents.setLevel(50)}
                else if (sVentsCmd == "75") {sVents.setLevel(75)}
            }
            if (sShades) {
                if (sShadesCmd == "open") {sShades.setLevel(100)}
            	else if (sShadesCmd == "close") {sShades.setLevel(0)}
                else if (sShadesCmd == "25") {sShades.setLevel(25)}
                else if (sShadesCmd == "50") {sShades.setLevel(50)}
                else if (sShadesCmd == "75") {sShades.setLevel(75)}
            }
            if (cTstat) { thermostats() }
            if (cTstat1) { thermostats1() }
            if (pMode) { setLocationMode(pMode) }
            if (shmState) { sendLocationEvent(name: "alarmSystemStatus", value: shmState) }
            if (pRoutine) { location.helloHome.execute(settings.pRoutine) }
            if (pRoutine2) { location.helloHome.execute(settings.pRoutine2) }
            if (sFlash) { flashLights() }
        }
        logOut << ["type":"info", "text":"done running processActions..."]
        // The map this object returned is required by the profile DO NOT REVERT
        return [result:true, resp:(settings?.scResponse ?: null)]
    } catch (ex) {
        log.error "processActions error: $ex", ex
        logOut << ["type":"error", "text":"processActions error: $ex"]
        // The map this object is sending is required by the profile DO NOT REVERT
        return [result:false, resp:null]
    }
    atomicState?.logTest = logOut
    runIn(3, "logTest", [overwrite:true])
}

void logTest(){
    def logs = atomicState?.logTest ?: [:]
    logs.each {
        log."${it?.type}" "${it?.text.toString()}"
    }
    atomicState?.logTest = null
}


/************************************************************************************************************
    THERMOSTATS HANDLERS
************************************************************************************************************/
private thermostats() {
    cTstat.each {deviceD ->
        def currentMode = deviceD.currentValue("thermostatMode")
        def currentTMP = deviceD.currentValue("temperature")
        if (cTstatMode == "off") { cTstat.off()
            }
        if (cTstatMode == "auto" || cTstatMode == "on") {
            cTstat.auto()
            cTstat.setCoolingSetpoint(coolLvl)
            cTstat.setHeatingSetpoint(heatLvl)
            }
        if (cTstatMode == "cool") {
            cTstat.cool()
            cTstat.setCoolingSetpoint(coolLvl)
            }
        if (cTstatMode == "heat") {
            cTstat.heat()
            cTstat.setHeatingSetpoint(heatLvl)
            }
        if (cTstatMode == "incr") {
            def cNewSetpoint = tempChange
            cNewSetpoint = tempChange + currentTMP
            cNewSetpoint = cNewSetpoint < 60 ? 60 : cNewSetpoint > 85 ? 85 : cNewSetpoint
            def hNewSetpoint = tempChange
            hNewSetpoint = tempChange + currentTMP
            hNewSetpoint = hNewSetpoint < 60 ? 60 : hNewSetpoint > 85 ? 85 : hNewSetpoint
            if (currentMode == "auto" || currentMode == "on") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
            if (currentMode == "cool") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
            }
            if (currentMode == "heat") {
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
        }
        if (cTstatMode == "decr") {
            def cNewSetpoint = tempChange
            cNewSetpoint = currentTMP - tempChange
            cNewSetpoint = cNewSetpoint < 60 ? 60 : cNewSetpoint > 85 ? 85 : cNewSetpoint
            def hNewSetpoint = tempChange
            hNewSetpoint = currentTMP - tempChange
            hNewSetpoint = hNewSetpoint < 60 ? 60 : hNewSetpoint > 85 ? 85 : hNewSetpoint
            if (currentMode == "auto" || currentMode == "on") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
            if (currentMode == "cool") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
            }
            if (currentMode == "heat") {
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
        }
        if (cTstatFan == "auto" || cTstatFan == "off") { cTstat.fanAuto() }
        if (cTstatFan == "on") { cTstat.fanOn() }
        if (cTstatFan == "circ") { cTstat.fanCirculate() }
    }
}
private thermostats1() {
	cTstat1.each {deviceD ->
		def currentMode = deviceD.currentValue("thermostatMode")
		def currentTMP = deviceD.currentValue("temperature")
		if (cTstat1Mode == "off") { cTstat1.off()
        	}
        if (cTstat1Mode == "auto" || cTstat1Mode == "on") {
        	cTstat1.auto()
            cTstat1.setCoolingSetpoint(coolLvl1)
            cTstat1.setHeatingSetpoint(heatLvl1)
            }
        if (cTstat1Mode == "auto" || cTstat1Mode == "on") {
            cTstat1.auto()
            cTstat1.setCoolingSetpoint(coolLvl1)
            cTstat1.setHeatingSetpoint(heatLvl1)
            }
		if (cTstat1Mode == "incr") {
            	def cNewSetpoint = tempChange1
					cNewSetpoint = tempChange1 + currentTMP
					cNewSetpoint = cNewSetpoint < 60 ? 60 : cNewSetpoint > 85 ? 85 : cNewSetpoint
            	def hNewSetpoint = tempChange1
					hNewSetpoint = tempChange1 + currentTMP
					hNewSetpoint = hNewSetpoint < 60 ? 60 : hNewSetpoint > 85 ? 85 : hNewSetpoint
            		if (currentMode == "auto" || currentMode == "on") {
                    	deviceD.setCoolingSetpoint(cNewSetpoint)
                        deviceD.setHeatingSetpoint(hNewSetPoint)
                        }
                    if (currentMode == "cool") {
            			deviceD.setCoolingSetpoint(cNewSetpoint)
                		}
            		if (currentMode == "heat") {
            			deviceD.setHeatingSetpoint(hNewSetPoint)
                	}
        		}
		if (cTsta1tMode == "decr") {
            	def cNewSetpoint = tempChange1
					cNewSetpoint = currentTMP - tempChange1
					cNewSetpoint = cNewSetpoint < 60 ? 60 : cNewSetpoint > 85 ? 85 : cNewSetpoint
            	def hNewSetpoint = tempChange1
					hNewSetpoint = currentTMP - tempChange1
					hNewSetpoint = hNewSetpoint < 60 ? 60 : hNewSetpoint > 85 ? 85 : hNewSetpoint
            		if (currentMode == "auto" || currentMode == "on") {
                    	deviceD.setCoolingSetpoint(cNewSetpoint)
                        deviceD.setHeatingSetpoint(hNewSetPoint)
                        }
                    if (currentMode == "cool") {
            			deviceD.setCoolingSetpoint(cNewSetpoint)
                		}
            		if (currentMode == "heat") {
            			deviceD.setHeatingSetpoint(hNewSetPoint)
                	}
        		}
        if (cTstat1Fan == "auto" || cTstat1Fan == "off") { cTstat1.fanAuto() }
        if (cTstat1Fan == "on") { cTstat1.fanOn() }
        if (cTstat1Fan == "circ") { cTstat1.fanCirculate() }
	}
}

/************************************************************************************************************
    TOGGLE SWITCHES HANDLER
************************************************************************************************************/
private toggle1() {
    if (sSwitches) {
        if (sSwitches?.currentValue('switch').contains('on')) {
            sSwitches?.off()
        }
        else if (sSwitches?.currentValue('switch').contains('off')) {
            sSwitches?.on()
        }
    }
}
private toggle2() {
    if (sOtherSwitch) {
        if (sOtherSwitch?.currentValue('switch').contains('on')) {
            sOtherSwitch?.off()
        }
        else if (sOtherSwitch?.currentValue('switch').contains('off')) {
            sOtherSwitch?.on()
        }
    }
}
/************************************************************************************************************
        Momentary Devices Handler
************************************************************************************************************/
def momentaryDeviceHandler() {
       settings?.mSwitches?.on()
    runIn(settings?.mOff, "turnOff", [overwrite: true])
}

def turnOff() {
    mSwitches?.off()
}

/************************************************************************************************************
    FLASHING LIGHTS HANDLER
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
        def initialActionOn = sFlash.collect{it.currentflashSwitch != "on"}
        def delay = 0L

        numFlashes.times {
            sFlash.eachWithIndex {s, i ->
                if (initialActionOn[i]) {
                    s.on(delay: delay)
                }
                else {
                    s.off(delay:delay)
                }
            }
            delay += onFor
            sFlash.eachWithIndex {s, i ->
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

/******************************************************************************
    COLOR BULBS HANDLER
******************************************************************************/
private processColor() {
    if (sHuesCmd == "on") { sHues?.on() }
    if (sHuesCmd == "off") { sHues?.off() }
    if (sHuesOtherCmd == "on") { sHuesOther?.on() }
    if (sHuesOtherCmd == "off") { sHuesOther?.off() }
    def hueSetVals = getColorName("${sHuesColor}",level)
        sHues?.setColor(hueSetVals)
    def    hueSetValsOther = getColorName("${sHuesOtherColor}",level)
        sHuesOther?.setColor(hueSetValsOther)
}

private getColorName(sHuesColor, level) {
    for (color in fillColorSettings()) {
        if (color.name.toLowerCase() == sHuesColor.toLowerCase()) {
            int hueVal = Math.round(color.h / 3.6)
            int hueLevel = !level ? color.l : level
            def hueSet = [hue: hueVal, saturation: color.s, level: hueLevel]
            return hueSet
        }
    }
}

def fillColorSettings() {
    return [
        [ name: "Soft White",                rgb: "#B6DA7C",        h: 83,        s: 44,        l: 67,    ],
        [ name: "Warm White",                rgb: "#DAF17E",        h: 51,        s: 20,        l: 100,    ],
        [ name: "Very Warm White",            rgb: "#DAF17E",        h: 51,        s: 60,        l: 51,    ],
        [ name: "Daylight White",            rgb: "#CEF4FD",        h: 191,        s: 9,        l: 90,    ],
        [ name: "Daylight",                    rgb: "#CEF4FD",        h: 191,        s: 9,        l: 90,    ],
        [ name: "Cool White",                rgb: "#F3F6F7",        h: 187,        s: 19,        l: 96,    ],
        [ name: "White",                    rgb: "#FFFFFF",        h: 0,        s: 0,        l: 100,    ],
        [ name: "Alice Blue",                rgb: "#F0F8FF",        h: 208,        s: 100,        l: 97,    ],
        [ name: "Antique White",            rgb: "#FAEBD7",        h: 34,        s: 78,        l: 91,    ],
        [ name: "Aqua",                        rgb: "#00FFFF",        h: 180,        s: 100,        l: 50,    ],
        [ name: "Aquamarine",                rgb: "#7FFFD4",        h: 160,        s: 100,        l: 75,    ],
        [ name: "Azure",                    rgb: "#F0FFFF",        h: 180,        s: 100,        l: 97,    ],
        [ name: "Beige",                    rgb: "#F5F5DC",        h: 60,        s: 56,        l: 91,    ],
        [ name: "Bisque",                    rgb: "#FFE4C4",        h: 33,        s: 100,        l: 88,    ],
        [ name: "Blanched Almond",            rgb: "#FFEBCD",        h: 36,        s: 100,        l: 90,    ],
        [ name: "Blue",                        rgb: "#0000FF",        h: 240,        s: 100,        l: 50,    ],
        [ name: "Blue Violet",                rgb: "#8A2BE2",        h: 271,        s: 76,        l: 53,    ],
        [ name: "Brown",                    rgb: "#A52A2A",        h: 0,        s: 59,        l: 41,    ],
        [ name: "Burly Wood",                rgb: "#DEB887",        h: 34,        s: 57,        l: 70,    ],
        [ name: "Cadet Blue",                rgb: "#5F9EA0",        h: 182,        s: 25,        l: 50,    ],
        [ name: "Chartreuse",                rgb: "#7FFF00",        h: 90,        s: 100,        l: 50,    ],
        [ name: "Chocolate",                rgb: "#D2691E",        h: 25,        s: 75,        l: 47,    ],
        [ name: "Coral",                    rgb: "#FF7F50",        h: 16,        s: 100,        l: 66,    ],
        [ name: "Corn Flower Blue",            rgb: "#6495ED",        h: 219,        s: 79,        l: 66,    ],
        [ name: "Corn Silk",                rgb: "#FFF8DC",        h: 48,        s: 100,        l: 93,    ],
        [ name: "Crimson",                    rgb: "#DC143C",        h: 348,        s: 83,        l: 58,    ],
        [ name: "Cyan",                        rgb: "#00FFFF",        h: 180,        s: 100,        l: 50,    ],
        [ name: "Dark Blue",                rgb: "#00008B",        h: 240,        s: 100,        l: 27,    ],
        [ name: "Dark Cyan",                rgb: "#008B8B",        h: 180,        s: 100,        l: 27,    ],
        [ name: "Dark Golden Rod",            rgb: "#B8860B",        h: 43,        s: 89,        l: 38,    ],
        [ name: "Dark Gray",                rgb: "#A9A9A9",        h: 0,        s: 0,        l: 66,    ],
        [ name: "Dark Green",                rgb: "#006400",        h: 120,        s: 100,        l: 20,    ],
        [ name: "Dark Khaki",                rgb: "#BDB76B",        h: 56,        s: 38,        l: 58,    ],
        [ name: "Dark Magenta",                rgb: "#8B008B",        h: 300,        s: 100,        l: 27,    ],
        [ name: "Dark Olive Green",            rgb: "#556B2F",        h: 82,        s: 39,        l: 30,    ],
        [ name: "Dark Orange",                rgb: "#FF8C00",        h: 33,        s: 100,        l: 50,    ],
        [ name: "Dark Orchid",                rgb: "#9932CC",        h: 280,        s: 61,        l: 50,    ],
        [ name: "Dark Red",                    rgb: "#8B0000",        h: 0,        s: 100,        l: 27,    ],
        [ name: "Dark Salmon",                rgb: "#E9967A",        h: 15,        s: 72,        l: 70,    ],
        [ name: "Dark Sea Green",            rgb: "#8FBC8F",        h: 120,        s: 25,        l: 65,    ],
        [ name: "Dark Slate Blue",            rgb: "#483D8B",        h: 248,        s: 39,        l: 39,    ],
        [ name: "Dark Slate Gray",            rgb: "#2F4F4F",        h: 180,        s: 25,        l: 25,    ],
        [ name: "Dark Turquoise",            rgb: "#00CED1",        h: 181,        s: 100,        l: 41,    ],
        [ name: "Dark Violet",                rgb: "#9400D3",        h: 282,        s: 100,        l: 41,    ],
        [ name: "Deep Pink",                rgb: "#FF1493",        h: 328,        s: 100,        l: 54,    ],
        [ name: "Deep Sky Blue",            rgb: "#00BFFF",        h: 195,        s: 100,        l: 50,    ],
        [ name: "Dim Gray",                    rgb: "#696969",        h: 0,        s: 0,        l: 41,    ],
        [ name: "Dodger Blue",                rgb: "#1E90FF",        h: 210,        s: 100,        l: 56,    ],
        [ name: "Fire Brick",                rgb: "#B22222",        h: 0,        s: 68,        l: 42,    ],
        [ name: "Floral White",                rgb: "#FFFAF0",        h: 40,        s: 100,        l: 97,    ],
        [ name: "Forest Green",                rgb: "#228B22",        h: 120,        s: 61,        l: 34,    ],
        [ name: "Fuchsia",                    rgb: "#FF00FF",        h: 300,        s: 100,        l: 50,    ],
        [ name: "Gainsboro",                rgb: "#DCDCDC",        h: 0,        s: 0,        l: 86,    ],
        [ name: "Ghost White",                rgb: "#F8F8FF",        h: 240,        s: 100,        l: 99,    ],
        [ name: "Gold",                        rgb: "#FFD700",        h: 51,        s: 100,        l: 50,    ],
        [ name: "Golden Rod",                rgb: "#DAA520",        h: 43,        s: 74,        l: 49,    ],
        [ name: "Gray",                        rgb: "#808080",        h: 0,        s: 0,        l: 50,    ],
        [ name: "Green",                    rgb: "#008000",        h: 120,        s: 100,        l: 25,    ],
        [ name: "Green Yellow",                rgb: "#ADFF2F",        h: 84,        s: 100,        l: 59,    ],
        [ name: "Honeydew",                    rgb: "#F0FFF0",        h: 120,        s: 100,        l: 97,    ],
        [ name: "Hot Pink",                    rgb: "#FF69B4",        h: 330,        s: 100,        l: 71,    ],
        [ name: "Indian Red",                rgb: "#CD5C5C",        h: 0,        s: 53,        l: 58,    ],
        [ name: "Indigo",                    rgb: "#4B0082",        h: 275,        s: 100,        l: 25,    ],
        [ name: "Ivory",                    rgb: "#FFFFF0",        h: 60,        s: 100,        l: 97,    ],
        [ name: "Khaki",                    rgb: "#F0E68C",        h: 54,        s: 77,        l: 75,    ],
        [ name: "Lavender",                    rgb: "#E6E6FA",        h: 240,        s: 67,        l: 94,    ],
        [ name: "Lavender Blush",            rgb: "#FFF0F5",        h: 340,        s: 100,        l: 97,    ],
        [ name: "Lawn Green",                rgb: "#7CFC00",        h: 90,        s: 100,        l: 49,    ],
        [ name: "Lemon Chiffon",            rgb: "#FFFACD",        h: 54,        s: 100,        l: 90,    ],
        [ name: "Light Blue",                rgb: "#ADD8E6",        h: 195,        s: 53,        l: 79,    ],
        [ name: "Light Coral",                rgb: "#F08080",        h: 0,        s: 79,        l: 72,    ],
        [ name: "Light Cyan",                rgb: "#E0FFFF",        h: 180,        s: 100,        l: 94,    ],
        [ name: "Light Golden Rod Yellow",    rgb: "#FAFAD2",        h: 60,        s: 80,        l: 90,    ],
        [ name: "Light Gray",                rgb: "#D3D3D3",        h: 0,        s: 0,        l: 83,    ],
        [ name: "Light Green",                rgb: "#90EE90",        h: 120,        s: 73,        l: 75,    ],
        [ name: "Light Pink",                rgb: "#FFB6C1",        h: 351,        s: 100,        l: 86,    ],
        [ name: "Light Salmon",                rgb: "#FFA07A",        h: 17,        s: 100,        l: 74,    ],
        [ name: "Light Sea Green",            rgb: "#20B2AA",        h: 177,        s: 70,        l: 41,    ],
        [ name: "Light Sky Blue",            rgb: "#87CEFA",        h: 203,        s: 92,        l: 75,    ],
        [ name: "Light Slate Gray",            rgb: "#778899",        h: 210,        s: 14,        l: 53,    ],
        [ name: "Light Steel Blue",            rgb: "#B0C4DE",        h: 214,        s: 41,        l: 78,    ],
        [ name: "Light Yellow",                rgb: "#FFFFE0",        h: 60,        s: 100,        l: 94,    ],
        [ name: "Lime",                        rgb: "#00FF00",        h: 120,        s: 100,        l: 50,    ],
        [ name: "Lime Green",                rgb: "#32CD32",        h: 120,        s: 61,        l: 50,    ],
        [ name: "Linen",                    rgb: "#FAF0E6",        h: 30,        s: 67,        l: 94,    ],
        [ name: "Maroon",                    rgb: "#800000",        h: 0,        s: 100,        l: 25,    ],
        [ name: "Medium Aquamarine",        rgb: "#66CDAA",        h: 160,        s: 51,        l: 60,    ],
        [ name: "Medium Blue",                rgb: "#0000CD",        h: 240,        s: 100,        l: 40,    ],
        [ name: "Medium Orchid",            rgb: "#BA55D3",        h: 288,        s: 59,        l: 58,    ],
        [ name: "Medium Purple",            rgb: "#9370DB",        h: 260,        s: 60,        l: 65,    ],
        [ name: "Medium Sea Green",            rgb: "#3CB371",        h: 147,        s: 50,        l: 47,    ],
        [ name: "Medium Slate Blue",        rgb: "#7B68EE",        h: 249,        s: 80,        l: 67,    ],
        [ name: "Medium Spring Green",        rgb: "#00FA9A",        h: 157,        s: 100,        l: 49,    ],
        [ name: "Medium Turquoise",            rgb: "#48D1CC",        h: 178,        s: 60,        l: 55,    ],
        [ name: "Medium Violet Red",        rgb: "#C71585",        h: 322,        s: 81,        l: 43,    ],
        [ name: "Midnight Blue",            rgb: "#191970",        h: 240,        s: 64,        l: 27,    ],
        [ name: "Mint Cream",                rgb: "#F5FFFA",        h: 150,        s: 100,        l: 98,    ],
        [ name: "Misty Rose",                rgb: "#FFE4E1",        h: 6,        s: 100,        l: 94,    ],
        [ name: "Moccasin",                    rgb: "#FFE4B5",        h: 38,        s: 100,        l: 85,    ],
        [ name: "Navajo White",                rgb: "#FFDEAD",        h: 36,        s: 100,        l: 84,    ],
        [ name: "Navy",                        rgb: "#000080",        h: 240,        s: 100,        l: 25,    ],
        [ name: "Old Lace",                    rgb: "#FDF5E6",        h: 39,        s: 85,        l: 95,    ],
        [ name: "Olive",                    rgb: "#808000",        h: 60,        s: 100,        l: 25,    ],
        [ name: "Olive Drab",                rgb: "#6B8E23",        h: 80,        s: 60,        l: 35,    ],
        [ name: "Orange",                    rgb: "#FFA500",        h: 39,        s: 100,        l: 50,    ],
        [ name: "Orange Red",                rgb: "#FF4500",        h: 16,        s: 100,        l: 50,    ],
        [ name: "Orchid",                    rgb: "#DA70D6",        h: 302,        s: 59,        l: 65,    ],
        [ name: "Pale Golden Rod",            rgb: "#EEE8AA",        h: 55,        s: 67,        l: 80,    ],
        [ name: "Pale Green",                rgb: "#98FB98",        h: 120,        s: 93,        l: 79,    ],
        [ name: "Pale Turquoise",            rgb: "#AFEEEE",        h: 180,        s: 65,        l: 81,    ],
        [ name: "Pale Violet Red",            rgb: "#DB7093",        h: 340,        s: 60,        l: 65,    ],
        [ name: "Papaya Whip",                rgb: "#FFEFD5",        h: 37,        s: 100,        l: 92,    ],
        [ name: "Peach Puff",                rgb: "#FFDAB9",        h: 28,        s: 100,        l: 86,    ],
        [ name: "Peru",                        rgb: "#CD853F",        h: 30,        s: 59,        l: 53,    ],
        [ name: "Pink",                        rgb: "#FFC0CB",        h: 350,        s: 100,        l: 88,    ],
        [ name: "Plum",                        rgb: "#DDA0DD",        h: 300,        s: 47,        l: 75,    ],
        [ name: "Powder Blue",                rgb: "#B0E0E6",        h: 187,        s: 52,        l: 80,    ],
        [ name: "Purple",                    rgb: "#800080",        h: 300,        s: 100,        l: 25,    ],
        [ name: "Red",                        rgb: "#FF0000",        h: 0,        s: 100,        l: 50,    ],
        [ name: "Rosy Brown",                rgb: "#BC8F8F",        h: 0,        s: 25,        l: 65,    ],
        [ name: "Royal Blue",                rgb: "#4169E1",        h: 225,        s: 73,        l: 57,    ],
        [ name: "Saddle Brown",                rgb: "#8B4513",        h: 25,        s: 76,        l: 31,    ],
        [ name: "Salmon",                    rgb: "#FA8072",        h: 6,        s: 93,        l: 71,    ],
        [ name: "Sandy Brown",                rgb: "#F4A460",        h: 28,        s: 87,        l: 67,    ],
        [ name: "Sea Green",                rgb: "#2E8B57",        h: 146,        s: 50,        l: 36,    ],
        [ name: "Sea Shell",                rgb: "#FFF5EE",        h: 25,        s: 100,        l: 97,    ],
        [ name: "Sienna",                    rgb: "#A0522D",        h: 19,        s: 56,        l: 40,    ],
        [ name: "Silver",                    rgb: "#C0C0C0",        h: 0,        s: 0,        l: 75,    ],
        [ name: "Sky Blue",                    rgb: "#87CEEB",        h: 197,        s: 71,        l: 73,    ],
        [ name: "Slate Blue",                rgb: "#6A5ACD",        h: 248,        s: 53,        l: 58,    ],
        [ name: "Slate Gray",                rgb: "#708090",        h: 210,        s: 13,        l: 50,    ],
        [ name: "Snow",                        rgb: "#FFFAFA",        h: 0,        s: 100,        l: 99,    ],
        [ name: "Spring Green",                rgb: "#00FF7F",        h: 150,        s: 100,        l: 50,    ],
        [ name: "Steel Blue",                rgb: "#4682B4",        h: 207,        s: 44,        l: 49,    ],
        [ name: "Tan",                        rgb: "#D2B48C",        h: 34,        s: 44,        l: 69,    ],
        [ name: "Teal",                        rgb: "#008080",        h: 180,        s: 100,        l: 25,    ],
        [ name: "Thistle",                    rgb: "#D8BFD8",        h: 300,        s: 24,        l: 80,    ],
        [ name: "Tomato",                    rgb: "#FF6347",        h: 9,        s: 100,        l: 64,    ],
        [ name: "Turquoise",                rgb: "#40E0D0",        h: 174,        s: 72,        l: 56,    ],
        [ name: "Violet",                    rgb: "#EE82EE",        h: 300,        s: 76,        l: 72,    ],
        [ name: "Wheat",                    rgb: "#F5DEB3",        h: 39,        s: 77,        l: 83,    ],
        [ name: "White Smoke",                rgb: "#F5F5F5",        h: 0,        s: 0,        l: 96,    ],
        [ name: "Yellow",                    rgb: "#FFFF00",        h: 60,        s: 100,        l: 50,    ],
        [ name: "Yellow Green",                rgb: "#9ACD32",        h: 80,        s: 61,        l: 50,    ],
    ]
}
// PROFILE ACTIONS
def pDevicesSettings() {
     if (sDoor || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers || sHues || sHuesOther || sFlash || sFans || sShades || sVents || sLocks) {
        return "complete"
    }
     return ""
}
def pDevicesComplete() {
     if (sDoor || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers || sHues || sHuesOther || sFlash || sFans || sShades || sVents || sLocks) {
         return "Configured"
     }
     return "Tap here to select devices"
}
