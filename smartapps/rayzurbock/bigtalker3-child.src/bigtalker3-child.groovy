definition(
    name: "BigTalker3-Child-DEV",
    namespace: "rayzurbock",
    author: "rayzur@rayzurbock.com",
    description: "Do not install in the mobile app, Save don't publish (needed by BigTalker3)",
    category: "Fun & Social",
    parent: "rayzurbock:BigTalker3-Parent-DEV",
    iconUrl: "https://lowrance.cc/ST/icons/BigTalker.png",
    iconX2Url: "https://lowrance.cc/ST/icons/BigTalker@2x.png",
    iconX3Url: "https://lowrance.cc/ST/icons/BigTalker@2x.png")

preferences {
    page(name: "pageConfigureEvents")
    page(name: "pageConfigMotion")
    page(name: "pageConfigSwitch")
    page(name: "pageConfigPresence")
    page(name: "pageConfigLock")
    page(name: "pageConfigContact")
    page(name: "pageConfigMode")
    page(name: "pageConfigThermostat")
    page(name: "pageConfigAcceleration")
    page(name: "pageConfigWater")
    page(name: "pageConfigSmoke")
    page(name: "pageConfigButton")
    page(name: "pageConfigTime")
    page(name: "pageConfigPowerMeter")
	page(name: "pageConfigAlarm")
	page(name: "pageConfigFilterStatus")
    page(name: "pageConfigValve")
    page(name: "pageHelpPhraseTokens")
	
}

def pageConfigureEvents(){
    initFormattingVars()
    setVersion()
    state.hubType = parent.returnVar("hubType")
    dynamicPage(name: "pageConfigureEvents", title: "Configure Events", install: (!(app?.getInstallationState == true)), uninstall: (app?.getInstallationState == true)) {
        section("Group Settings:"){
            label(name: "labelRequired", title: "Event Group Name:", defaultValue: "Change this", required: true, multiple: false)
            input(name: "groupEnabled", type: "bool", title: "Enable Group", required: true, defaultValue: true)
            paragraph "Speech devices of all types (music players, speech speakers, audio notification devices) can be used together. Each device will receive only the actions it supports."
            input name: "testGroupNow", type: "bool", title: "Toggle to speak a test phrase for this group", required: false, defaultValue: false, submitOnChange: true
            if (settings?.testGroupNow == true && state?.lastTestGroupNow != true) {
                state.lastTestGroupNow = true
                sendTalk(app.label, "${app.label} event group is working correctly.", null, getDesiredVolume(null), false, false, getMyVoice(null), [displayName: "BigTalker Test", name: "EventGroupTest", value: "test", descriptionText: "Event group test"])
            } else if (settings?.testGroupNow != true) {
                state.lastTestGroupNow = false
            }
        }
        section("Configuration Warnings:"){
            def warnTypes = ["motion","switch","presence","lock","contact","thermostat","acceleration","water","smoke","button","alarm","powerMeter","valve"]
            def warnings = []
            warnTypes.each { t ->
                def hasDevices = (1..3).any { group -> settings."${t}DeviceGroup${group}" != null }
                def hasPhrase = settings.findAll { k, v -> k.toString().startsWith(t) && k.toString().contains("TalkOn") && v }
                if (hasDevices && !hasPhrase) { warnings << t }
            }
            if (warnings.isEmpty()) {
                paragraph "No configuration issues detected."
            } else {
                paragraph "<font color=orange>Warning: The following trigger type(s) have device(s) selected but no phrase configured: ${warnings.join(', ')}</font>"
            }
        }
        section("Talk on events:") {
            if (settings.timeSlotTime1 || settings.timeSlotTime2 || settings.timeSlotTime3) {
                href "pageConfigTime", title: "Time", description: "Tap to modify", state:"complete"
            } else {
                href "pageConfigTime", title: "Time", description: "Tap to configure"
            }
            if (settings.motionDeviceGroup1 || settings.motionDeviceGroup2 || settings.motionDeviceGroup3) {
                href "pageConfigMotion", title:"Motion", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigMotion", title:"Motion", description:"Tap to configure"
            }
            if (settings.switchDeviceGroup1 || settings.switchDeviceGroup2 || settings.switchDeviceGroup3) {
                href "pageConfigSwitch", title:"Switch", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigSwitch", title:"Switch", description:"Tap to configure"
            }
            if (settings.presenceDeviceGroup1 || settings.presenceDeviceGroup2 || settings.presenceDeviceGroup3) {
                href "pageConfigPresence", title:"Presence", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigPresence", title:"Presence", description:"Tap to configure"
            }
            if (settings.lockDeviceGroup1 || settings.lockDeviceGroup2 || settings.lockDeviceGroup3) {
                href "pageConfigLock", title:"Lock", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigLock", title:"Lock", description:"Tap to configure"
            }
            if (settings.contactDeviceGroup1 || settings.contactDeviceGroup2 || settings.contactDeviceGroup3) {
                href "pageConfigContact", title:"Contact", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigContact", title:"Contact", description:"Tap to configure"
            }
            if (settings.modePhraseGroup1 || settings.modePhraseGroup2 || settings.modePhraseGroup3) {
                href "pageConfigMode", title:"Mode", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigMode", title:"Mode", description:"Tap to configure"
            }
            if (settings.thermostatDeviceGroup1 || settings.thermostatDeviceGroup2 || settings.thermostatDeviceGroup3) {
                href "pageConfigThermostat", title:"Thermostat", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigThermostat", title:"Thermostat", description:"Tap to configure"
            }
            if (settings.accelerationDeviceGroup1 || settings.accelerationDeviceGroup2 || settings.accelerationDeviceGroup3) {
                href "pageConfigAcceleration", title: "Acceleration", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigAcceleration", title: "Acceleration", description:"Tap to configure"
            }
            if (settings.waterDeviceGroup1 || settings.waterDeviceGroup2 || settings.waterDeviceGroup3) {
                href "pageConfigWater", title: "Water", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigWater", title: "Water", description:"Tap to configure"
            }
            if (settings.smokeDeviceGroup1 || settings.smokeDeviceGroup2 || settings.smokeDeviceGroup3) {
                href "pageConfigSmoke", title: "Smoke", description:"Tap to modify", state:"complete"
            } else { 
                href "pageConfigSmoke", title: "Smoke", description:"Tap to configure"
            }
            if (settings.buttonDeviceGroup1 || settings.buttonDeviceGroup2 || settings.buttonDeviceGroup3) {
                href "pageConfigButton", title: "Button", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigButton", title: "Button", description:"Tap to configure"
            }
			if (settings.alarmDeviceGroup1 || settings.alarmDeviceGroup2 || settings.alarmDeviceGroup3) {
                href "pageConfigAlarm", title: "Alarm", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigAlarm", title: "Alarm", description:"Tap to configure"
            }
			if (settings.filterStatusGroup1 || settings.consumableDeviceGroup2 || settings.consumableDeviceGroup3) {
                href "pageConfigFilterStatus", title: "Filter Status", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigFilterStatus", title: "Filter Status", description:"Tap to configure"
            }
            if (settings.valveDeviceGroup1 || settings.valveDeviceGroup2 || settings.valveDeviceGroup3) {
                href "pageConfigValve", title:"Valve", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigValve", title:"Valve", description:"Tap to configure"
            }
            if (settings.powerMeterDeviceGroup1 || settings.powerMeterDeviceGroup2 || settings.powerMeterDeviceGroup3) {
                href "pageConfigPowerMeter", title: "Power Meter", description:"Tap to modify", state:"complete"
            } else {
                href "pageConfigPowerMeter", title: "Power Meter", description:"Tap to configure"
            }
        }
		updateCheck()
		//checkButtons()
		section(){
        	displayVersionStatus()
        }
    }
}

def pageConfigMotion(){

    dynamicPage(name: "pageConfigMotion", title: "Configure talk on motion", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//CONFIGURATION
		section(){
			def defaultSpeechActive1 = ""
            def defaultSpeechInactive1 = ""
            if (!motionDeviceGroup1) {
                defaultSpeechActive1 = "%devicename% is now %devicechange%"
                defaultSpeechInactive1 = "%devicename% is now %devicechange%"
            }
			input name: "motionDeviceGroup1", type: "capability.motionSensor", title: "${state.formatSettingRootStart}Motion Sensor(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "motionTalkOnActive1", type: "text", title: "${state.formatSettingRootStart}Say this on motion active:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechActive1, submitOnChange: true
            input name: "motionTestOnActive1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test motion active phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "motionTalkOnInactive1", type: "text", title: "${state.formatSettingRootStart}Say this on motion inactive:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechInactive1, submitOnChange: true
            input name: "motionTestOnInactive1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test motion inactive phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "motionPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"], submitOnChange: true
            input name: "motionMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false, submitOnChange: true
			input name: "motionSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false, submitOnChange: true
			input name: "motionAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false, submitOnChange: true
            if (true) {
            	input name: "motionVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            	input name: "motionResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true, submitOnChange: true
                input name: "motionVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${parent.SettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
		//RESTRICTIONS
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "motionModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "motionStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "motionEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.motionStartTime1 == null))
            input name: "motionDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            //input name: "motionCount1", type: "number", title: "${state.formatSettingOptionalEnd}Do this only x times (next prompt)...${parent.formatSettingOptionalEnd}", required: false, submitOnChange: true
            //input name: "motionCountUnit1", type:"enum", title: "${state.formatSettingOptionalEnd}... per ${parent.formatSettingOptionalEnd}", required: settings.motionCount1, options: ["Minute", "Hour", "Day"]
            input name: "motionDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
		def phraseTestTogDeviceUpper = "Motion"
		def phraseTestTogDeviceLower = "motion"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Active"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Inactive"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "motionActiveDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'active' only if still 'active' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "motionActiveDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Active' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "motionActiveRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'active' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "motionActiveRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Active' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "motionActiveRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'active' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "motionInactiveDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'inactive' only if still 'inactive' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "motionInactiveDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Inactive' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "motionInactiveRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'inactive' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "motionInactiveRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Inactive' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "motionInactiveRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'inactive' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigMotion()
}

def pageConfigSwitch(){
    dynamicPage(name: "pageConfigSwitch", title: "Configure talk on switch", install: false, uninstall: false) {
		//CONFIGURATION
        section(){
            def defaultSpeechOn1 = ""
            def defaultSpeechOff1 = ""
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
            if (!switchDeviceGroup1) {
                defaultSpeechOn1 = "%devicename% is now %devicechange%"
                defaultSpeechOff1 = "%devicename% is now %devicechange%"
            }
            input name: "switchDeviceGroup1", type: "capability.switch", title: "${state.formatSettingRootStart}Switch(es)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "switchTalkOnOn1", type: "text", title: "${state.formatSettingRootStart}Say this when switch is turned ON:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechOn1, submitOnChange: true
            input name: "switchTestOnOn1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test switch ON phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "switchTalkOnOff1", type: "text", title: "${state.formatSettingRootStart}Say this when switch is turned OFF:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechOff1, submitOnChange: true
            input name: "switchTestOnOff1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test switch OFF phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "switchPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"], submitOnChange: true
            input name: "switchMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false, submitOnChange: true
			input name: "switchSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false, submitOnChange: true
			input name: "switchAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false, submitOnChange: true
            if (true) {
            	input name: "switchVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            	input name: "switchResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true, submitOnChange: true
                input name: "switchVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
		//RESTRICTIONS
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            //IN DEVELOPMENT  input name: "switchOnThreshold", type: "number", title: "${state.formatSettingOptionalStart}If it's on for more than this many minutes (default 0)${state.formatSettingOptionalEnd}", required: false, defaultValue: 0
            input name: "switchModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "switchStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "switchEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.switchStartTime1 == null))
            input name: "switchDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "switchDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Switch"
		def phraseTestTogDeviceLower = "switch"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "On"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Off"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "switchOnDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'on' only if still 'on' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "switchOnDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'On' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "switchOnRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'on' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "switchOnRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'On' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "switchOnRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'on' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "switchOffDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'off' only if still 'off' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "switchOffDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Off' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "switchOffRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'off' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "switchOffRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Off' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "switchOffRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'off' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigSwitch()
}

def pageConfigPresence(){
    dynamicPage(name: "pageConfigPresence", title: "Configure talk on presence", install: false, uninstall: false) {
		//CONFIGURATION
        section(){
            def defaultSpeechArrive1 = ""
            def defaultSpeechLeave1 = ""
			def testState = ""
            if (!presenceDeviceGroup1) {
                defaultSpeechArrive1 = "%devicename% has arrived"
                defaultSpeechLeave1 = "%devicename% has left"
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
            }
            input name: "presenceDeviceGroup1", type: "capability.presenceSensor", title: "${state.formatSettingRootStart}Presence Sensor(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "presenceTalkOnPresent1", type: "text", title: "${state.formatSettingRootStart}Say this when someone arrives:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechArrive1
			input name: "presenceTestOnPresent1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test arrival phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "presenceTalkOnNotpresent1", type: "text", title: "${state.formatSettingRootStart}Say this when someone leaves:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechLeave1
			input name: "presenceTestOnNotpresent1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test departure phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "presencePersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "presenceMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalEnd}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "presenceSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalEnd}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "presenceAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalEnd}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "presenceVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
            	input name: "presenceResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "presenceVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
		//RESTRICTIONS
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "presenceModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "presenceStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "presenceEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.presenceStartTime1 == null))
            input name: "presenceDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "presenceDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Presence"
		def phraseTestTogDeviceLower = "presence"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Present"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Not present"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "presencePresentDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'present' only if still 'present' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "presencePresentDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Present' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "presencePresentRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'present' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "presencePresentRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Present' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "presencePresentRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'present' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "presenceNotpresentDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'not present' only if still 'not present' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "presenceNotpresentDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Notpresent' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "presenceNotpresentRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'not present' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "presenceNotpresentRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Notpresent' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "presenceNotpresentRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'not present' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigPresence()
}

def pageConfigLock(){
    dynamicPage(name: "pageConfigLock", title: "Configure talk on lock", install: false, uninstall: false) {
		//CONFIGURATION
        section(){
            def defaultSpeechUnlock1 = ""
            def defaultSpeechLock1 = ""
            if (!lockDeviceGroup1) {
                defaultSpeechUnlock1 = "%devicename% is now unlocked"
                defaultSpeechLock1 = "%devicename% is now locked"
            }
            input name: "lockDeviceGroup1", type: "capability.lock", title: "${state.formatSettingRootStart}Lock(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "lockTalkOnUnlock1", type: "text", title: "${state.formatSettingRootStart}Say this when unlocked:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechUnlock1
			input name: "lockTestOnUnlock1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test unlock phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "lockTalkOnLock1", type: "text", title: "${state.formatSettingRootStart}Say this when locked:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechLock1
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
			input name: "lockTestOnLock1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test lock phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "lockPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "lockMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "lockSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "lockAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "lockVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
            	input name: "lockResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "lockVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
		//RESTRICTIONS
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "lockModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "lockStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "lockEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.lockStartTime1 == null))
            input name: "lockDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s${state.formatSettingOptionalEnd})", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "lockDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Lock"
		def phraseTestTogDeviceLower = "lock"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Lock"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Unlock"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "lockLockedDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'locked' only if still 'locked' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "lockLockedDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Locked' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "lockLockedRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'locked' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "lockLockedRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Locked' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "lockLockedRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'locked' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "lockUnlockedDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'unlocked' only if still 'unlocked' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "lockUnlockedDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Unlocked' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "lockUnlockedRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'unlocked' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "lockUnlockedRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Unlocked' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "lockUnlockedRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'unlocked' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigLock()
}

def pageConfigContact(){
    dynamicPage(name: "pageConfigContact", title: "Configure talk on contact sensor", install: false, uninstall: false) {
		//CONFIGURATION
        section(){
            def defaultSpeechOpen1 = ""
            def defaultSpeechClosed1 = ""
            if (!contactDeviceGroup1) {
                defaultSpeechOpen1 = "%devicename% is now %devicechange%"
                defaultSpeechClosed1 = "%devicename% is now %devicechange%"
            }
            input name: "contactDeviceGroup1", type: "capability.contactSensor", title: "${state.formatSettingRootStart}Contact sensor(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "contactTalkOnOpen1", type: "text", title: "${state.formatSettingRootStart}Say this when opened:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechOpen1
			input name: "contactTestOnOpen1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test opened phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "contactTalkOnClosed1", type: "text", title: "${state.formatSettingRootStart}Say this when closed:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechClosed1
			input name: "contactTestOnClosed1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test closed phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "contactPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingRootEnd}", required: false, options: ["Yes", "No"]
            input name: "contactMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "contactSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
			input name: "contactAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "contactVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "contactResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "contactVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
		//RESTRICTIONS
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            //IN DEVELOPMENT input name: "contactOpenThreshold", type: "number", title: "If it's open for more than this many minutes (default 0)", required: false, defaultValue: 0
            input name: "contactModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "contactStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "contactEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.contactStartTime1 == null))
            input name: "contactDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "contactDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Contact"
		def phraseTestTogDeviceLower = "contact"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Open"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Closed"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "contactOpenDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'open' only if still 'open' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "contactOpenDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Open' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "contactOpenRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'open' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "contactOpenRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Open' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "contactOpenRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'open' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "contactClosedDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'closed' only if still 'closed' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "contactClosedDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Closed' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "contactClosedRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'closed' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "contactClosedRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Closed' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "contactClosedRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'closed' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigContact()
}

def pageConfigMode(){

    def locationmodes = []
    location.modes.each(){
       locationmodes += it
    }
    LOGDEBUG("locationmodes=${locationmodes}", true)
    dynamicPage(name: "pageConfigMode", title: "Configure talk on home mode change", install: false, uninstall: false) {
		//CONFIGURATION
        section(){
            def defaultSpeechMode1 = ""
            if (!modePhraseGroup1) {
                defaultSpeechMode1 = "%locationname% mode has changed from %lastmode% to %mode%"
            }
            input name: "modePhraseGroup1", type:"mode", title:"${state.formatSettingRootStart}When mode changes to:${state.formatSettingRootEnd}", required:false, multiple:true, submitOnChange:false
            input name: "modeExcludePhraseGroup1", type: "mode", title: "${state.formatSettingOptionalStart}But not when changed from (optional):${state.formatSettingOptionalEnd}", required: false, multiple: true
            input name: "modeTalkOnChange1", type: "text", title: "${state.formatSettingRootStart}Say this when home mode is changed${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechMode1
			input name: "modeTestOnChange1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test mode change phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "modePersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "modePhraseMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "modePhraseSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "modePhraseAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "modePhraseVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "modePhraseResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "modePhraseVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//RESTRICTIONS
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "modeStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "modeEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: false//(!(settings.modeStartTime1 == null))
            input name: "modeDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "modeDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Mode"
		def phraseTestTogDeviceLower = "mode"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Change"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
    }
//End pageConfigMode()
}

def pageConfigThermostat(){
    dynamicPage(name: "pageConfigThermostat", title: "Configure talk when thermostat state is:", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section(){
            def defaultSpeechIdle1 = ""
            def defaultSpeechHeating1 = ""
            def defaultSpeechCooling1 = ""
            def defaultSpeechFan1 = ""
            if (!thermostatDeviceGroup1) {
                defaultSpeechIdle1 = "%devicename% is now off"
                defaultSpeechHeating1 = "%devicename% is now heating"
                defaultSpeechCooling1 = "%devicename% is now cooling"
                defaultSpeechFan1 = "%devicename% is now circulating fan"
            }
            input name: "thermostatDeviceGroup1", type: "capability.thermostat", title: "${state.formatSettingRootStart}Thermostat(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "thermostatTalkOnIdle1", type: "text", title: "${state.formatSettingRootStart}Say this on change to Idle:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechIdle1
			input name: "thermostatTestOnIdle1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test Idle phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "thermostatTalkOnHeating1", type: "text", title: "${state.formatSettingRootStart}Say this on change to heating:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechHeating1
			input name: "thermostatTestOnHeating1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test Heating phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "thermostatTalkOnCooling1", type: "text", title: "${state.formatSettingRootStart}Say this on change to cooling:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechCooling1
			input name: "thermostatTestOnCooling1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test Cooling phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "thermostatTalkOnFanonly1", type: "text", title: "${state.formatSettingRootStart}Say this on change to fan only:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechFan1
			input name: "thermostatTestOnFanonly1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test Fan phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "thermostatPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "thermostatMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "thermostatSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "thermostatAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "thermostatVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
            	input name: "thermostatResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "thermostatVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "thermostatModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "thermostatStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "thermostatEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.thermostatStartTime1 == null))
            input name: "thermostatDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "thermostatDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Thermostat"
		def phraseTestTogDeviceLower = "thermostat"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Idle"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Heating"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Cooling"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Fanonly"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "thermostatIdleDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'idle' only if still 'idle' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "thermostatIdleDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Idle' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "thermostatIdleRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'idle' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "thermostatIdleRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Idle' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "thermostatIdleRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'idle' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "thermostatHeatingDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'heating' only if still 'heating' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "thermostatHeatingDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Heating' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "thermostatHeatingRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'heating' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "thermostatHeatingRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Heating' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "thermostatHeatingRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'heating' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "thermostatCoolingDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'cooling' only if still 'cooling' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "thermostatCoolingDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Cooling' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "thermostatCoolingRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'cooling' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "thermostatCoolingRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Cooling' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "thermostatCoolingRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'cooling' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "thermostatFanonlyDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'fan only' only if still 'fan only' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "thermostatFanonlyDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Fanonly' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "thermostatFanonlyRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'fan only' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "thermostatFanonlyRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Fanonly' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "thermostatFanonlyRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'fan only' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigThermostat()
}

def pageConfigAcceleration(){
    dynamicPage(name: "pageConfigAcceleration", title: "Configure talk on acceleration", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section(){
            def defaultSpeechActive1 = ""
            def defaultSpeechInactive1 = ""
            if (!accelerationDeviceGroup1) {
                defaultSpeechActive1 = "%devicename% acceleration %devicechange%"
                defaultSpeechInactive1 = "%devicename% acceleration is no longer active"
            }
            input name: "accelerationDeviceGroup1", type: "capability.accelerationSensor", title: "${state.formatSettingRootStart}Acceleration sensor(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "accelerationTalkOnActive1", type: "text", title: "${state.formatSettingRootStart}Say this when activated:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechActive1
			input name: "accelerationTestOnActive1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test activate phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "accelerationTalkOnInactive1", type: "text", title: "${state.formatSettingRootStart}Say this when inactivated:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechInactive1
			input name: "accelerationTestOnInactive1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test inactivate phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "accelerationPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "accelerationMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "accelerationSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "accelerationAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "accelerationVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
            	input name: "accelerationResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "accelerationVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "accelerationModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "accelerationStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "accelerationEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.accelerationStartTime1 == null))
            input name: "accelerationDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "accelerationDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Acceleration"
		def phraseTestTogDeviceLower = "acceleration"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Active"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Inactive"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "accelerationActiveDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'active' only if still 'active' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "accelerationActiveDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Active' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "accelerationActiveRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'active' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "accelerationActiveRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Active' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "accelerationActiveRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'active' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "accelerationInactiveDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'inactive' only if still 'inactive' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "accelerationInactiveDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Inactive' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "accelerationInactiveRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'inactive' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "accelerationInactiveRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Inactive' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "accelerationInactiveRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'inactive' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigAcceleration()
}

def pageConfigWater(){
    dynamicPage(name: "pageConfigWater", title: "Configure talk on water", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section(){
            def defaultSpeechWet1 = ""
            def defaultSpeechDry1 = ""
            if (!waterDeviceGroup1) {
                defaultSpeechWet1 = "%devicename% is %devicechange%"
                defaultSpeechDry1 = "%devicename% is %devicechange%"
            }
            input name: "waterDeviceGroup1", type: "capability.waterSensor", title: "${state.formatSettingRootStart}Water sensor(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "waterTalkOnWet1", type: "text", title: "${state.formatSettingRootStart}Say this when wet:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechWet1
			input name: "waterTestOnWet1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test wet phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "waterTalkOnDry1", type: "text", title: "${state.formatSettingRootStart}Say this when dry:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechDry1
			input name: "waterTestOnDry1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test dry phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "waterPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "waterMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "waterSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "waterAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "waterVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "waterResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "waterVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "waterModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "waterStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "waterEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.waterStartTime1 == null))
            input name: "waterDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "waterDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Water"
		def phraseTestTogDeviceLower = "water"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Wet"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Dry"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "waterWetDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'wet' only if still 'wet' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "waterWetDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Wet' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "waterWetRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'wet' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "waterWetRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Wet' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "waterWetRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'wet' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "waterDryDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'dry' only if still 'dry' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "waterDryDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Dry' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "waterDryRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'dry' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "waterDryRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Dry' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "waterDryRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'dry' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigWater()
}

def pageConfigSmoke(){
    dynamicPage(name: "pageConfigSmoke", title: "Configure talk on smoke", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section(){
            def defaultSpeechDetect1 = ""
            def defaultSpeechClear1 = ""
            def defaultSpeechTested1 = ""
            if (!smokeDeviceGroup1) {
                defaultSpeechDetect1 = "Smoke, %devicename% has detected smoke"
                defaultSpeechClear1 = "Smoke, %devicename% has cleared smoke alert"
                defaultSpeechTested1 = "Smoke, %devicename% has been tested"
            }
            input name: "smokeDeviceGroup1", type: "capability.smokeDetector", title: "${state.formatSettingRootStart}Smoke detector(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "smokeTalkOnDetected1", type: "text", title: "${state.formatSettingRootStart}Say this when detected:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechDetect1
			input name: "smokeTestOnDetected1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test smoke detected phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "smokeTalkOnClear1", type: "text", title: "${state.formatSettingRootStart}Say this when cleared:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechClear1
			input name: "smokeTestOnClear1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test smoke cleared phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "smokeTalkOnTested1", type: "text", title: "${state.formatSettingRootStart}Say this when tested:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechTested1
			input name: "smokeTestOnTested1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test smoke tested phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "smokePersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "smokeMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "smokeSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "smokeAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "smokeVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "smokeResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "smokeVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "smokeModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "smokeStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "smokeEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.smokeStartTime1 == null))
            input name: "smokeDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "smokeDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Smoke"
		def phraseTestTogDeviceLower = "smoke"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Detect"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Clear"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Tested"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "smokeDetectedDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'detected' only if still 'detected' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "smokeDetectedDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Detected' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "smokeDetectedRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'detected' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "smokeDetectedRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Detected' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "smokeDetectedRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'detected' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "smokeClearDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'clear' only if still 'clear' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "smokeClearDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Clear' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "smokeClearRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'clear' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "smokeClearRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Clear' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "smokeClearRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'clear' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "smokeTestedDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'tested' only if still 'tested' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "smokeTestedDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Tested' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "smokeTestedRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'tested' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "smokeTestedRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Tested' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "smokeTestedRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'tested' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigSmoke()
}

def pageConfigButton(){
    dynamicPage(name: "pageConfigButton", title: "Configure talk on button press", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section(){
            def defaultSpeechButton1 = ""
            def defaultSpeechButtonHold1 = ""
            if (!buttonDeviceGroup1) {
                defaultSpeechButton1 = "%devicename% button pressed"
                defaultSpeechButtonHold1 = "%devicename% button held"
            }
            input name: "buttonDeviceGroup1", type: "capability.button", title: "${state.formatSettingRootStart}Button(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "buttonTalkOnPressed1", type: "text", title: "${state.formatSettingRootStart}Say this when pressed:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechButton1
			input name: "buttonTestOnPressed1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test pressed phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "buttonTalkOnHeld1", type: "text", title: "${state.formatSettingRootStart}Say this when held:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechButtonHold1
			input name: "buttonTestOnHeld1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test held phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "buttonPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "buttonMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "buttonSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "buttonAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "buttonVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "buttonResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "buttonVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "buttonModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "buttonStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "buttonEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.buttonStartTime1 == null))
            input name: "buttonDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "buttonDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Button"
		def phraseTestTogDeviceLower = "button"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Pressed"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Held"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "buttonPushedDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'pushed' only if still 'pushed' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "buttonPushedDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Pushed' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "buttonPushedRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'pushed' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "buttonPushedRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Pushed' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "buttonPushedRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'pushed' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "buttonHeldDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'held' only if still 'held' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "buttonHeldDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Held' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "buttonHeldRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'held' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "buttonHeldRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Held' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "buttonHeldRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'held' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigButton()
}

def pageConfigAlarm(){

    dynamicPage(name: "pageConfigAlarm", title: "Configure talk on alarm", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section(){
            def defaultSpeechAlarmOff1 = ""
            def defaultSpeechAlarmBoth1 = ""
			def defaultSpeechAlarmSiren1 = ""
			def defaultSpeechAlarmStrobe1 = ""
            if (!alarmDeviceGroup1) {
                defaultSpeechAlarmOff1 = "%devicename% alarm is now off"
                defaultSpeechAlarmBoth1 = "%devicename% alarm siren and strobe are now active"
				defaultSpeechAlarmSiren1 = "%devicename% alarm siren is now active"
				defaultSpeechAlarmStrobe1 = "%devicename% alarm strobe is now active"
            }
            input name: "alarmDeviceGroup1", type: "capability.alarm", title: "${state.formatSettingRootStart}Alarm(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "alarmTalkOnOff1", type: "text", title: "${state.formatSettingRootStart}Say this when turned off:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechAlarmOff1
			input name: "alarmTestOnOff1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test off phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "alarmTalkOnBoth1", type: "text", title: "${state.formatSettingRootStart}Say this when siren and strobe are active:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechAlarmBoth1
			input name: "alarmTestOnBoth1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test siren and strobe active phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
			input name: "alarmTalkOnSiren1", type: "text", title: "${state.formatSettingRootStart}Say this when only the siren is active:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechAlarmSiren1
			input name: "alarmTestOnSiren1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test siren phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
			input name: "alarmTalkOnStrobe1", type: "text", title: "${state.formatSettingRootStart}Say this when only the strobe is active:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechAlarmStrobe1
			input name: "alarmTestOnStrobe1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test strobe phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "alarmPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "alarmMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "alarmSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "alarmAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "alarmVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "alarmResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "alarmVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "alarmModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "alarmStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "alarmEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.alarmStartTime1 == null))
            input name: "alarmDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "alarmDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "Alarm"
		def phraseTestTogDeviceLower = "alarm"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Off"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Both"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Siren"
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Strobe"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "alarmOffDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'off' only if still 'off' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "alarmOffDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Off' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "alarmOffRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'off' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "alarmOffRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Off' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "alarmOffRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'off' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "alarmStrobeDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'strobe' only if still 'strobe' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "alarmStrobeDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Strobe' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "alarmStrobeRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'strobe' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "alarmStrobeRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Strobe' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "alarmStrobeRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'strobe' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "alarmSirenDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'siren' only if still 'siren' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "alarmSirenDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Siren' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "alarmSirenRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'siren' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "alarmSirenRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Siren' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "alarmSirenRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'siren' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "alarmBothDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'both' only if still 'both' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "alarmBothDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Both' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "alarmBothRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'both' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "alarmBothRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Both' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "alarmBothRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'both' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigAlarm()
}

def pageConfigFilterStatus(){

    dynamicPage(name: "pageConfigFilterStatus", title: "Configure talk on Filter Status change", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section(){
            def defaultSpeechFilterStatusNormal1 = ""
            def defaultSpeechFilterStatusReplace1 = ""
            if (!filteStatusDeviceGroup1) {
                defaultSpeechFilterStatusNormal1 = "%devicename% filter is normal"
                defaultSpeechFilterStatusReplace1 = "%devicename% filter needs to be replaced"
            }
            input name: "filterStatusGroup1", type: "capability.filterstatus", title: "${state.formatSettingRootStart}Filter(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "filterStatusTalkOnNormal1", type: "text", title: "${state.formatSettingRootStart}Say this when filter status changes to normal:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechFilterStatusNormal1
			input name: "filterStatusTestOnNormal1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test normal phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "filterStatusTalkOnReplace1", type: "text", title: "${state.formatSettingRootStart}Say this when filter status changes to replace:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechFilterStatusReplace1
			input name: "filterStatusTestOnReplace1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test replace phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "filterStatusPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "filterStatusMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "filterStatusSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "filterStatusAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "filterStatusVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "filterStatusResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "filterStatusVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "filterStatusModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "filterStatusStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "filterStatusEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.filterStatusStartTime1 == null))
            input name: "filterStatusDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "filterStatusDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "FilterStatus"
		def phraseTestTogDeviceLower = "filterStatus"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Normal"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Replace"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "filterStatusNormalDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'normal' only if still 'normal' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "filterStatusNormalDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Normal' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "filterStatusNormalRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'normal' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "filterStatusNormalRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Normal' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "filterStatusNormalRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'normal' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "filterStatusReplaceDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'replace' only if still 'replace' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "filterStatusReplaceDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Replace' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "filterStatusReplaceRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'replace' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "filterStatusReplaceRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Replace' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "filterStatusReplaceRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'replace' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigFilterStatus()
}


def pageConfigTime(){
    dynamicPage(name: "pageConfigTime", title: "Configure talk at specific time", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section("Schedule 1"){
            input name: "timeSlotDays1", type: "enum", title: "${state.formatSettingRootStart}Which day(s)${state.formatSettingRootEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "timeSlotTime1", type: "time", title: "${state.formatSettingRootStart}Time of day${state.formatSettingRootEnd}", required: false
            input name: "timeSlotTalkOnTime1", type: "text", title: "${state.formatSettingRootStart}Say on schedule:${state.formatSettingRootEnd}", required: false
			input name: "timeSlotTestOnTime1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test schedule phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "timeSlotPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "timeSlotMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "timeSlotSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "timeSlotAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "timeSlotVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "timeSlotResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "timeSlotVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("Restrictions (Schedule 1)"){
            input name: "timeSlotModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "timeSlotDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
		section("Schedule 2"){
            input name: "timeSlotDays2", type: "enum", title: "${state.formatSettingRootStart}Which day(s)${state.formatSettingRootEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "timeSlotTime2", type: "time", title: "${state.formatSettingRootStart}Time of day${state.formatSettingRootEnd}", required: false
            input name: "timeSlotTalkOnTime2", type: "text", title: "${state.formatSettingRootStart}Say on schedule:${state.formatSettingRootEnd}", required: false
			input name: "timeSlotTestOnTime2", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test schedule phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "timeSlotPersonality2", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "timeSlotMusicPlayerDevice2", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "timeSlotSpeechSynthesisDevice2", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "timeSlotAudioNotificationDevice2", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "timeSlotVolume2", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "timeSlotResumePlay2", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "timeSlotVoice2", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("Restrictions (Schedule 2)"){
            input name: "timeSlotModes2", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "timeSlotDisableSwitch2", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
		section("Schedule 3"){
            input name: "timeSlotDays3", type: "enum", title: "${state.formatSettingRootStart}Which day(s)${state.formatSettingRootEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "timeSlotTime3", type: "time", title: "${state.formatSettingRootStart}Time of day${state.formatSettingRootEnd}", required: false
            input name: "timeSlotTalkOnTime3", type: "text", title: "${state.formatSettingRootStart}Say on schedule:${state.formatSettingRootEnd}", required: false
			input name: "timeSlotTestOnTime3", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test schedule phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "timeSlotPersonality3", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "timeSlotMusicPlayerDevice3", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "timeSlotSpeechSynthesisDevice3", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "timeSlotAudioNotificationDevice3", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "timeSlotVolume3", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "timeSlotResumePlay3", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "timeSlotVoice3", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("Restrictions (Schedule 3)"){
            input name: "timeSlotModes3", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "timeSlotDisableSwitch3", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//Test Phrase Toggle Switch - Deviates from standard template
		def phraseTestTogDeviceUpper = "TimeSlot"
		def phraseTestTogDeviceLower = "timeSlot"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Time1"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState}", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" //capture toggle switch state
        }
		phraseTestTogState = "Time2"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice2") { myVoice = settings?."${phraseTestTogDeviceLower}Voice2" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState}", getCombinedSpeechDevices(phraseTestTogDeviceLower, 2), settings."${phraseTestTogDeviceLower}Volume2", settings."${phraseTestTogDeviceLower}ResumePlay2", settings."${phraseTestTogDeviceLower}Personality2", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" //capture toggle switch state
        }
		phraseTestTogState = "Time3"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice3") { myVoice = settings?."${phraseTestTogDeviceLower}Voice3" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState}", getCombinedSpeechDevices(phraseTestTogDeviceLower, 3), settings."${phraseTestTogDeviceLower}Volume3", settings."${phraseTestTogDeviceLower}ResumePlay3", settings."${phraseTestTogDeviceLower}Personality3", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState}" //capture toggle switch state
        }
    }
}

def pageConfigPowerMeter(){
    dynamicPage(name: "pageConfigPowerMeter", title: "Configure talk on power consumption", install: false, uninstall: false) {
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section(){
            def defaultSpeechPowerMeterRise1 = ""
            def defaultSpeechPowerMeterNormal1 = ""
            def defaultSpeechPowerMeterFall1 = ""
            state.powerMeterState = ""
            if (!powerMeterDeviceGroup1) {
                defaultSpeechPowerMeterRise1 = "%devicename% power level is high at %value% watts"
                defaultSpeechPowerMeterNormal1 = "%devicename% power level is within normal range"
                defaultSpeechPowerMeterFall1 = "%devicename% power level is low at %value% watts"
            }
            input name: "powerMeterDeviceGroup1", type: "capability.powerMeter", title: "${state.formatSettingRootStart}Power Meter(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "powerMeterTalkOnRise1", type: "text", title: "${state.formatSettingRootStart}Say this if power rises above threshold:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechPowerMeterRise1, submitOnChange: true
			input name: "powerMeterTestOnRise1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test power rise phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "powerMeterTalkOnFall1", type: "text", title: "${state.formatSettingRootStart}Say this if power falls below threshold:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechPowerMeterFall1, submitOnChange: true
			input name: "powerMeterTestOnFall1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test power fall phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "powerMeterTalkOnNormal1", type: "text", title: "${state.formatSettingRootStart}Say this if power returns to normal:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechPowerMeterNormal1, submitOnChange: false
			input name: "powerMeterTestOnNormal1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test power normal phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "powerMeterTalkOnRiseThold1", type: "number", title: "${state.formatSettingRootStart}High energy usage threshold (watts):${state.formatSettingRootEnd}", required: powerMeterTalkOnRise1, defaultValue: defaultSpeechpowerMeterRise1
            input name: "powerMeterTalkOnFallThold1", type: "number", title: "${state.formatSettingRootStart}Low energy usage threshold (watts):${state.formatSettingRootEnd}", required: powerMeterTalkOnFall1, defaultValue: defaultSpeechpowerMeterFall1
            input name: "powerMeterPersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"]
            input name: "powerMeterMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false
			input name: "powerMeterSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false
			input name: "powerMeterAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false
            if (true) {
            	input name: "powerMeterVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false
                input name: "powerMeterResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true
                input name: "powerMeterVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "powerMeterModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "powerMeterStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "powerMeterEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.powerMeterStartTime1 == null))
            input name: "powerMeterDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "powerMeterDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
		//Test Phrase Toggle Switch
		def phraseTestTogDeviceUpper = "PowerMeter"
		def phraseTestTogDeviceLower = "powerMeter"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Rise"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Fall"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Normal"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
    }
//End pageConfigpowerMeter()
}


def pageConfigValve(){

    dynamicPage(name: "pageConfigValve", title: "Configure talk on valve", install: false, uninstall: false) {
		//CONFIGURATION
        section(){
            def defaultSpeechOpen1 = ""
            def defaultSpeechClosed1 = ""
            if (!switchDeviceGroup1) {
                defaultSpeechOpen1 = "%devicename% is now %devicechange%"
                defaultSpeechClosed1 = "%devicename% is now %devicechange%"
            }
            input name: "valveDeviceGroup1", type: "capability.valve", title: "${state.formatSettingRootStart}Valve(s)${state.formatSettingRootEnd}", required: false, multiple: true
            input name: "valveTalkOnOpen1", type: "text", title: "${state.formatSettingRootStart}Say this when valve is opened:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechOpen1, submitOnChange: true
            input name: "valveTestOnOpen1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test valve OPENED phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "valveTalkOnClosed1", type: "text", title: "${state.formatSettingRootStart}Say this when valve is closed:${state.formatSettingRootEnd}", required: false, defaultValue: defaultSpeechClosed1, submitOnChange: true
            input name: "valveTestOnClosed1", type: "bool", title: "${state.formatSettingOptionalStart}Toggle to test valve CLOSED phrase${state.formatSettingOptionalEnd}", required: false, defaultValue: false, submitOnChange: true
            input name: "valvePersonality1", type: "enum", title: "${state.formatSettingOptionalStart}Allow Personality (overrides default)?:${state.formatSettingOptionalEnd}", required: false, options: ["Yes", "No"], submitOnChange: true
            input name: "valveMusicPlayerDevice1", type: "capability.musicPlayer", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Music players (e.g. Sonos, VLCThing)", multiple: true, required: false, submitOnChange: true
			input name: "valveSpeechSynthesisDevice1", type: "capability.speechSynthesis", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Speech speakers (e.g. Echo, Google Home, LANnouncer, VLCThing)", multiple: true, required: false, submitOnChange: true
			input name: "valveAudioNotificationDevice1", type: "capability.audioNotification", title: "${state.formatSettingOptionalStart}Talk with these text-to-speech devices (overrides default)${state.formatSettingOptionalEnd} - Devices that play notification tracks directly", multiple: true, required: false, submitOnChange: true
            if (true) {
            	input name: "valveVolume1", type: "number", title: "${state.formatSettingOptionalStart}Set volume to (overrides default):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            	input name: "valveResumePlay1", type: "bool", title: "${state.formatSettingOptionalStart}Attempt to resume playing audio?${state.formatSettingOptionalEnd}", required: false, defaultValue: (parent.returnVar("resumePlay") == false) ? false : true, submitOnChange: true
                input name: "valveVoice1", type: "enum", title: "${state.formatSettingOptionalStart}Voice (overrides default):${state.formatSettingOptionalEnd}", options: parent.returnVar("supportedVoices"), required: false, submitOnChange: true
            }
        }
		//RESTRICTIONS
		//HELP
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Help${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
        section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Restrictions${state.formatCenterEnd}${state.formatSettingRootEnd}"){
            input name: "valveModes1", type: "mode", title: "${state.formatSettingOptionalStart}Talk when in these mode(s) (overrides default)${state.formatSettingOptionalEnd}", multiple: true, required: false
            input name: "valveStartTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk before (overrides default)${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
            input name: "valveEndTime1", type: "time", title: "${state.formatSettingOptionalStart}Don't talk after (overrides default)${state.formatSettingOptionalEnd}", required: (!(settings.valveStartTime1 == null))
            input name: "valveDays1", type: "enum", title: "${state.formatSettingOptionalStart}Restrict to these day(s)${state.formatSettingOptionalEnd}", required: false, options: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], multiple: true
            input name: "valveDisableSwitch1", type: "capability.switch", title: "${state.formatSettingOptionalStart}Disable when this switch is off${state.formatSettingOptionalEnd}", required: false, multiple: false
        }
		//Test Phrase Toggle Valve
		def phraseTestTogDeviceUpper = "Valve"
		def phraseTestTogDeviceLower = "valve"
		def phraseTestTogState = ""
		def testEvent = ""
		def myVoice = ""
		phraseTestTogState = "Open"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		phraseTestTogState = "Closed"
		if (state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null) {state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = false} //init var
		if ((!(settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" == null)) && (settings?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" != state?."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1")) { //test toggle switch state
			testEvent = [displayName: "Big Talker Test", name: "${phraseTestTogDeviceUpper}${phraseTestTogState}Test", value: phraseTestTogState]
            myVoice = parent.returnVar("speechVoice")
			if (settings?."${phraseTestTogDeviceLower}Voice1") { myVoice = settings?."${phraseTestTogDeviceLower}Voice1" }
			sendTalk(app.label, settings."${phraseTestTogDeviceLower}TalkOn${phraseTestTogState.replace(' ','')}1", getCombinedSpeechDevices(phraseTestTogDeviceLower, 1), settings."${phraseTestTogDeviceLower}Volume1", settings."${phraseTestTogDeviceLower}ResumePlay1", settings."${phraseTestTogDeviceLower}Personality1", myVoice, testEvent)
			state."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" = settings."${phraseTestTogDeviceLower}TestOn${phraseTestTogState.replace(' ','')}1" //capture toggle switch state
        }
		//DELAYED / REPEATED ANNOUNCEMENTS
		section("${state.formatBr}${state.formatHr}${state.formatHr}${state.formatSettingRootStart}${state.formatCenterStart}Delayed / Repeated Announcements${state.formatCenterEnd}${state.formatSettingRootEnd}"){
			paragraph "${state.formatSettingOptionalStart}Delay an announcement until the device has stayed in the selected state for the delay period (the state is rechecked before speaking). Optionally repeat the announcement while the state persists, with an optional cap on total announcements per event. A delay of 0 announces immediately (default behavior).${state.formatSettingOptionalEnd}"
			input name: "valveOpenDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'open' only if still 'open' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "valveOpenDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Open' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "valveOpenRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'open' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "valveOpenRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Open' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "valveOpenRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'open' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
			input name: "valveClosedDelayAmount1", type: "number", title: "${state.formatSettingOptionalStart}Announce 'closed' only if still 'closed' after (0 = announce immediately):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "valveClosedDelayUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Closed' delay unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "valveClosedRepeatEveryAmount1", type: "number", title: "${state.formatSettingOptionalStart}Repeat 'closed' announcement every (0 = do not repeat):${state.formatSettingOptionalEnd}", required: false, defaultValue: 0, submitOnChange: true
			input name: "valveClosedRepeatEveryUnit1", type: "enum", title: "${state.formatSettingOptionalStart}'Closed' repeat unit:${state.formatSettingOptionalEnd}", required: false, options: ["Seconds", "Minutes", "Hours"], defaultValue: "Minutes", submitOnChange: true
			input name: "valveClosedRepeatLimit1", type: "number", title: "${state.formatSettingOptionalStart}Stop after this many 'closed' announcements (blank = unlimited):${state.formatSettingOptionalEnd}", required: false, submitOnChange: true
		}
    }
//End pageConfigValve()
}


def pageHelpPhraseTokens(){

	//KEEP IN SYNC WITH PARENT!
        dynamicPage(name: "pageHelpPhraseTokens", title: "Available Phrase Tokens", install: false, uninstall:false){
       section("The following tokens can be used in your event phrases and will be replaced as listed:"){
       	   def AvailTokens = ""
           AvailTokens += "${state.formatUlStart}"
		   AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%groupname%${state.formatStrongEnd} = Name that you gave for the event group${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%date%${state.formatStrongEnd} = Current date; January 01 20xx${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%day%${state.formatStrongEnd} = Current day; Monday${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%devicename%${state.formatStrongEnd} = Triggering devices display name${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%devicetype%${state.formatStrongEnd} = Triggering device type; motion, switch, etc${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%devicechange%${state.formatStrongEnd} = State change that occurred; on/off, active/inactive, etc...${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%description%${state.formatStrongEnd} = The description of the event that is to be displayed to the user in the mobile application.${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%locationname%${state.formatStrongEnd} = Hub location name; home, work, etc${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%lastmode%${state.formatStrongEnd} = Last hub mode; home, away, etc${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%mode%${state.formatStrongEnd} = Current hub mode; home, away, etc${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%mp3(url)%${state.formatStrongEnd} = Play hosted MP3 file; URL should be http://www.domain.com/path/file.mp3"
           AvailTokens += "${state.formatUlStart}${state.formatLiStart}${state.formatIStart}No other tokens or phrases can be used with %mp3(url)%${state.formatIEnd}${state.formatLiEnd}${state.formatUlEnd}"
		   AvailTokens += "${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%time%${state.formatStrongEnd} = Current hub time; HH:mm am/pm${state.formatStrongEnd}${state.formatLiEnd}"
		   AvailTokens += "${state.formatUlEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%weathercurrent%${state.formatStrongEnd} = Current weather based on hub location${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%weathertoday%${state.formatStrongEnd} = Today's weather forecast* based on hub location${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%weathertonight%${state.formatStrongEnd} = Tonight's weather forecast* based on hub location${state.formatLiEnd}"
           AvailTokens += "${state.formatLiStart}${state.formatStrongStart}%weathertomorrow%${state.formatStrongEnd} = Tomorrow's weather forecast* based on hub location${state.formatLiEnd}"
           paragraph(AvailTokens)
       }
   }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

def initialize() {
    if (state.groupEnabled == true || state.groupEnabled == "true" || state.groupEnabled == null) {
    	app.updateLabel("${app.label.replace(" (disabled)","")}")
        initSchedule()
        initSubscribe()
        state.lastMode = location.mode
    }
    LOGTRACE("Initialized (Parent: ${parent.returnVar("version")}; Child Version: ${state.version}; Group Enabled: ${settings.groupEnabled})")
//End initialize()
}
def updated() {
    state.groupEnabled = settings.groupEnabled
	setVersion()
	LOGTRACE("Updating settings (Parent Version: ${parent.returnVar("version")}; Child Version: ${state.version}; Group Enabled: ${state.groupEnabled})")
	state.installed = true
	unschedule()
	state.delayQueue = []
	unsubscribe()
    if (state.groupEnabled == true || state.groupEnabled == "true" || state.groupEnabled == null) { 
        initialize() 
    } else {
        if (!(app.label.contains(" (disabled)"))) { app.updateLabel("${app.label.replace("${app.label}","${app.label} (disabled)")}") }
	}
}
def installed() {
	state.groupEnabled = settings.groupEnabled
	setVersion()
	LOGTRACE("Installed (Parent Version: ${parent.returnVar("version")}; Child Version: ${state.version}; Group Enabled: ${state.groupEnabled})")
	state.installed = true
	if (state.groupEnabled == true || state.groupEnabled == "true" || state.groupEnabled == null) { 
        initialize() 
    } else {
        if (!(app.label.contains(" (disabled)"))) { app.updateLabel("${app.label.replace("${app.label}","${app.label} (disabled)")}") }
	}
}

def initSubscribe(){
    //NOTICE: Only call from initialize()!
    LOGDEBUG ("BEGIN initSubscribe()", true)
    //Subscribe Motions
    if (motionDeviceGroup1) { subscribe(motionDeviceGroup1, "motion", onMotion1Event) }
    //Subscribe Switches
    if (switchDeviceGroup1) { subscribe(switchDeviceGroup1, "switch", onSwitch1Event) }
    //Subscribe Presence
    if (presenceDeviceGroup1) { subscribe(presenceDeviceGroup1, "presence", onPresence1Event); LOGDEBUG("Subscribed to presence: ${presenceDeviceGroup1}",false) }
    //Subscribe Lock
    if (lockDeviceGroup1) { subscribe(lockDeviceGroup1, "lock", onLock1Event) }
    //Subscribe Contact
    if (contactDeviceGroup1) { subscribe(contactDeviceGroup1, "contact", onContact1Event) }
    //Subscribe Thermostat
    if (thermostatDeviceGroup1) { subscribe(thermostatDeviceGroup1, "thermostatOperatingState", onThermostat1Event) }
    //Subscribe Acceleration
    if (accelerationDeviceGroup1) { subscribe(accelerationDeviceGroup1, "acceleration", onAcceleration1Event) }
    //Subscribe Water
    if (waterDeviceGroup1) { subscribe(waterDeviceGroup1, "water", onWater1Event) }
    //Subscribe Smoke
    if (smokeDeviceGroup1) { subscribe(smokeDeviceGroup1, "smoke", onSmoke1Event) }
    //Subscribe Button
    if (buttonDeviceGroup1) { subscribe(buttonDeviceGroup1, "button", onButton1Event) }
	//Subscribe Alarm
    if (alarmDeviceGroup1) { subscribe(alarmDeviceGroup1, "alarm", onAlarm1Event) }
	//Subscribe Filter Status
    if (filterStatusGroup1) { subscribe(filterStatusGroup1, "filterstatus", onFilterStatus1Event) }
	//Subscribe Oven Mode
    if (ovenModeDeviceGroup1) { subscribe(ovenModeDeviceGroup1, "ovenmode", onOvenMode1Event) }
    //Subscribe PowerMeter
    if (powerMeterDeviceGroup1) { subscribe(powerMeterDeviceGroup1, "power", onPowerMeter1Event) }
    //Subscribe Mode
    if (modePhraseGroup1) { subscribe(location, "mode", onModeChangeEvent) }
    //Subscribe Valve
    if (valveDeviceGroup1) { subscribe(valveDeviceGroup1, "valve", onValve1Event) }
    
    LOGDEBUG ("END initSubscribe()", true)
}

def initSchedule(){
    LOGDEBUG ("BEGIN initSchedule()", true)
    //Subscribe Schedule
    if (timeSlotTime1) { schedule(timeSlotTime1, onSchedule1Event) }
    if (timeSlotTime2) { schedule(timeSlotTime2, onSchedule2Event) }
    if (timeSlotTime3) { schedule(timeSlotTime3, onSchedule3Event) }
    LOGDEBUG ("END initSchedule()", true)
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// PROCESS RESTRICTIONS
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

def processRestrictions(devicetype,index){
	def allowed = true
    if (!(processDisableSwitch(devicetype,index))){
    	LOGDEBUG("RESTRICTION: Disable switch is set to disable", true)
        allowed = false
    } else {
        LOGDEBUG("RESTRICTION: Not restricted by switch", true)
    }
	if (!(dayAllowed(devicetype,index))) {
    	LOGDEBUG("RESTRICTION: Remain silent today", true)
        allowed = false
    } else {
        LOGDEBUG("RESTRICTION: Not restricted by day", true)
    }
	if (!(timeAllowed(devicetype,index))) {
    	LOGDEBUG("RESTRICTION: Remain silent in current time period", true)
        allowed = false
    } else {
        LOGDEBUG("RESTRICTION: Not restricted by time period", true)
    }
    if (!(devicetype == "mode")) {
    	if (!(modeAllowed(devicetype,index))) {
    		LOGDEBUG("RESTRICTION: Remain silent while in mode ${location.mode}", true)
        	allowed = false
    	} else {
        	LOGDEBUG("RESTRICTION: Not restricted by mode", true)
    	}
    }
    //if (!(processCountRestriction(devicetype,index))){
    //}
    return allowed
}

def stringToDate(strToConvert){
	return Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", strToConvert)
}

def timeAllowed(devicetype,index){
    def now = new Date()
    //Check Default Setting
    //devicetype = mode, motion, switch, presence, lock, contact, thermostat, acceleration, water, smoke, button
	if (!(settings."${devicetype}StartTime${index}" == null)){
		if (timeOfDayIsBetween(stringToDate(settings."${devicetype}StartTime${index}"), stringToDate(settings."${devicetype}EndTime${index}"), now, location.timeZone)) { return true } else { return false }
	}
    //No overrides have returned True, process Default
    if (parent.returnVar("defaultStartTime") == null) { 
    	return true 
    } else {
        if (timeOfDayIsBetween(parent.returnVar("defaultStartTime"), parent.returnVar("defaultEndTime"), now, location.timeZone)) { return true } else { return false }
    }
}

def modeAllowed(devicetype,index) {
    //Determine if we are allowed to speak in our current mode based on the calling device or default setting
    //devicetype = motion, switch, presence, lock, contact, thermostat, acceleration, water, smoke, button
	if (settings."${devicetype}Modes${index}") {
		if (settings."${devicetype}Modes${index}".contains(location.mode)){
			//Custom mode for this event is in use and we are in one of those modes
			return true
		} else {
			//Custome mode for this event is in use and we are not in one of those modes
			return false
		}
	} else {
		return (parent.returnVar("speechModesDefault").contains(location.mode)) //True if we are in an allowed Default mode, False if not
	}
}

def dayAllowed(devicetype,index){
	def allowedDays = []
	allowedDays = settings?."${devicetype}Days${index}"
	return processDayRestriction(allowedDays)
}

def processDayRestriction(allowedDays){
    def todayStr = (new Date().format('EEEE', location.timeZone))
    LOGDEBUG("Today=${todayStr}, Allowed:${allowedDays}", false)
    if (allowedDays == null || allowedDays == ""){
        return true
    }
    if (allowedDays.contains(todayStr) || allowedDays == null) {
        return true
    } else {
        return false
    }
}

def processDisableSwitch(devicetype, index) {
	//LOGDEBUG("processDisableSwitch: ${devicetype}DisableSwitch${index}", false)
	try{
		if (settings?."${devicetype}DisableSwitch${index}".currentSwitch == "on" || settings?."${devicetype}DisableSwitch${index}" == null) {
            LOGDEBUG("processDisableSwitch returning TRUE", true)
            return true
        } else {
            LOGDEBUG("processDisableSwitch returning False", true)
            return false
        }
	} catch(err) {
        LOGDEBUG("processDisableSwitch returning TRUE(Due to Error response)", true)
		return true
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//// General Functions
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

def getTimeFromDateString(inputtime, includeAmPm){
    //I couldn't find the way to do this in ST / Groovy, so I made my own function
    //Obtains the time from a supplied specifically formatted date string (ie: from a preference of type "time")
    //LOGDEBUG "InputTime: ${inputtime}", true"$
    def outputtime = inputtime
    def am_pm = "??"
    outputtime = inputtime.substring(11,16)
    if (includeAmPm) {
        if ((outputtime.substring(0,2)).toInteger() < 12) { 
            am_pm = "am" 
        } else { 
            am_pm = "pm"
            def newHH = ((outputtime.substring(0,2)).toInteger() - 12)
            outputtime = newHH + outputtime.substring(2,5)
        }
        outputtime += am_pm
    }
    //LOGDEBUG "OutputTime: ${outputtime}", true"$
    return outputtime
}

def getTimeFromCalendar(includeSeconds, includeAmPm){
    //Obtains the current time:  HH:mm:ss am/pm
    def calendar = Calendar.getInstance()
	calendar.setTimeZone(location.timeZone)
	def timeHH = calendar.get(Calendar.HOUR)
    def timemm = calendar.get(Calendar.MINUTE)
    def timess = calendar.get(Calendar.SECOND)
    def timeampm = calendar.get(Calendar.AM_PM) ? "pm" : "am"
    def timestring = "${timeHH}:${timemm}"
    if (includeSeconds) { timestring += ":${timess}" }
    if (includeAmPm) { timestring += " ${timeampm}" }
    return timestring
}

//myRunIn from ST:Geko / Statusbits SmartAlarm app http://statusbits.github.io/smartalarm/
private def myRunIn(delay_s, func) {
    //LOGDEBUG("myRunIn(${delay_s},${func})", true)

    if (delay_s > 0) {
        def tms = now() + (delay_s * 1000)
        def date = new Date(tms)
        runOnce(date, func)
        //LOGDEBUG("'${func}' scheduled to run at ${date}", true)
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// HANDLE EVENTS
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
def handleDeviceEvent(evt, deviceType, deviceStates) {
    // deviceStates is expected to be a list containing 4 elements: [state1, state2, state3, state4]
    def index = 1
    def deviceState1 = deviceStates.size() > 0 ? deviceStates[0] : ""
    def deviceState2 = deviceStates.size() > 1 ? deviceStates[1] : ""
    def deviceState3 = deviceStates.size() > 2 ? deviceStates[2] : ""
    def deviceState4 = deviceStates.size() > 3 ? deviceStates[3] : ""

    if (!(evt?.device.name == null)) { state."${deviceType}${index}EventDisplayName" = evt.device.name }
    if (!(evt?.displayName == null)) { state."${deviceType}${index}EventDisplayName" = evt.displayName }
    if (!(evt?.device.displayName == null)) { state."${deviceType}${index}EventDisplayName" = evt.device.displayName }

    state."${deviceType}${index}EventDisplayName" = evt.displayName
    state."${deviceType}${index}EventName" = evt.name
    state."${deviceType}${index}EventValue" = evt.value
    state."${deviceType}${index}EventTime" = now()

    LOGDEBUG("(handleDeviceEvent) StateSet for ${deviceType}: ${state."${deviceType}${index}EventName"} - ${state."${deviceType}${index}EventValue"}", true)

    // Delegate to the original core event processor
    processEvent(deviceType, deviceState1, deviceState2, deviceState3, deviceState4, index, evt)
}

// BEGIN HANDLE MOTIONS
def onMotion1Event(evt){
    handleDeviceEvent(evt, "motion", ["active", "inactive", "", ""])
}
// END HANDLE MOTIONS

// BEGIN HANDLE SWITCHES
def onSwitch1Event(evt){
    handleDeviceEvent(evt, "switch", ["on", "off", "", ""])
}
// END HANDLE SWITCHES


// BEGIN HANDLE PRESENCE
def onPresence1Event(evt){
    handleDeviceEvent(evt, "presence", ["present", "not present", "", ""])
}
// END HANDLE PRESENCE

// BEGIN HANDLE LOCK
def onLock1Event(evt){
    handleDeviceEvent(evt, "lock", ["locked", "unlocked", "", ""])
}
// END HANDLE LOCK

// BEGIN HANDLE CONTACT
def onContact1Event(evt){
    handleDeviceEvent(evt, "contact", ["open", "closed", "", ""])
}
// END HANDLE CONTACT

// BEGIN HANDLE THERMOSTAT
def onThermostat1Event(evt){
    handleDeviceEvent(evt, "thermostat", ["idle", "heating", "cooling", "fan only"])
}
// END HANDLE THERMOSTAT

// BEGIN HANDLE ACCELERATION
def onAcceleration1Event(evt){
    handleDeviceEvent(evt, "acceleration", ["active", "inactive", "", ""])
}
// END HANDLE ACCELERATION

// BEGIN HANDLE WATER
def onWater1Event(evt){
    handleDeviceEvent(evt, "water", ["wet", "dry", "", ""])
}
// END HANDLE WATER

// BEGIN HANDLE SMOKE
def onSmoke1Event(evt){
    handleDeviceEvent(evt, "smoke", ["detected", "clear", "tested", ""])
}
// END HANDLE SMOKE

// BEGIN HANDLE BUTTON
def onButton1Event(evt){
    handleDeviceEvent(evt, "button", ["pushed", "held", "", ""])
}
// END HANDLE BUTTON

// BEGIN HANDLE ALARM
def onAlarm1Event(evt){
    handleDeviceEvent(evt, "alarm", ["off", "strobe", "siren", "both"])
}
// END HANDLE ALARM

// BEGIN HANDLE FILTER STATUS
def onFilterStatus1Event(evt){
    handleDeviceEvent(evt, "filterStatus", ["normal", "replace", "", ""])
}
// END HANDLE FILTER STATUS

// BEGIN HANDLE OVEN MODE
def onOvenMode1Event(evt){
    handleDeviceEvent(evt, "ovenMode", ["heating", "grill", "warming", "defrosting"])
}
// END HANDLE OVEN MODE

// BEGIN HANDLE VALVE
def onValve1Event(evt){
    handleDeviceEvent(evt, "valve", ["open", "closed", "", ""])
}
// END HANDLE VALVE

////BEGIN PROCESSEVENT
def processEvent(deviceType, deviceState1, deviceState2, deviceState3, deviceState4, index, evt){
	def myVoice = getMyVoice(settings?."${deviceType}Voice${index}")
	LOGDEBUG("(processEvent(): ${evt.name}, ${index}, ${evt.value}, ${myVoice}", true)
    //Check Restrictions
	if (!(processRestrictions("${deviceType}",index))){ return }
    def deviceStates = [deviceState1, deviceState2, deviceState3, deviceState4]
    //Delay / recheck: if this event matches a known state that has a delay (or repeat) configured, queue it instead of announcing immediately
    def matchedState = deviceStates.find { it != null && it != "" && evt?.value?.toString() == it }
    if (matchedState) {
        def phraseKey = "${deviceType}TalkOn${matchedState.replace(' ','').capitalize()}${index}"
        if (!(settings?."${phraseKey}")) {
            LOGDEBUG("Not configured to speak for this event (${evt.value})", true)
            return
        }
        def delaySec = getDelaySeconds(deviceType, matchedState, index)
        def repeatSec = getRepeatSeconds(deviceType, matchedState, index)
        if (delaySec > 0) {
            queueDelayedAnnounce(deviceType, index, deviceStates, matchedState, evt, delaySec, repeatSec, 0)
            return
        }
        announceEvent(deviceType, index, deviceStates, evt, myVoice)
        if (repeatSec > 0) {
            //Announced immediately; schedule repeats while the state persists
            queueDelayedAnnounce(deviceType, index, deviceStates, matchedState, evt, repeatSec, repeatSec, 1)
        }
        return
    }
    announceEvent(deviceType, index, deviceStates, evt, myVoice)
}
////END PROCESSEVENT

//Announce the phrase configured for the event's current state (shared by the immediate and delayed/repeat paths)
def announceEvent(deviceType, index, deviceStates, evt, myVoice){
    def deviceState1 = deviceStates.size() > 0 ? deviceStates[0] : ""
    def deviceState2 = deviceStates.size() > 1 ? deviceStates[1] : ""
    def deviceState3 = deviceStates.size() > 2 ? deviceStates[2] : ""
    def deviceState4 = deviceStates.size() > 3 ? deviceStates[3] : ""
	def resume = ""; resume = parent.returnVar("resumePlay"); if (resume == "") { resume = true }
    def personality = ""; personality = parent.returnVar("personalityMode"); if (personality == "" || personality == null) { personality = false }
    def myVolume = -1
    state.TalkPhrase = null
    state.speechDevice = null
	if (true) {
		if (index == 1) {
			if (!(settings?."${deviceType}ResumePlay${index}" == null)) { resume = settings."${deviceType}ResumePlay${index}" }
		}
        if (resume == null) { resume = true }
	} else { resume = false }
	if (settings?."${deviceType}Personality${index}" == "Yes") {personality = true}
	if (settings?."${deviceType}Personality${index}" == "No") {personality = false}
    if (evt.value == deviceState1) {
		state.TalkPhrase = settings."${deviceType}TalkOn${deviceState1.replace(' ','').capitalize()}${index}"; state.speechDevice = getCombinedSpeechDevices(deviceType, index); myVolume = getDesiredVolume(settings."${deviceType}Volume${index}")
        if (!(state?.TalkPhrase == null)) {sendTalk(app.label,state.TalkPhrase, state.speechDevice, myVolume, resume, personality, myVoice, evt)} else {LOGDEBUG("Not configured to speak for this event (${evt.value} <> ${deviceState1})", true)}
    }
    if (evt.value == deviceState2) {
        state.TalkPhrase = settings."${deviceType}TalkOn${deviceState2.replace(' ','').capitalize()}${index}"; state.speechDevice = getCombinedSpeechDevices(deviceType, index); myVolume = getDesiredVolume(settings."${deviceType}Volume${index}")
        if (!(state?.TalkPhrase == null)) {sendTalk(app.label,state.TalkPhrase, state.speechDevice, myVolume, resume, personality, myVoice, evt)} else {LOGDEBUG("Not configured to speak for this event (${evt.value} <> ${deviceState2})", true)}
    }
	if (evt.value == deviceState3) {
        state.TalkPhrase = settings."${deviceType}TalkOn${deviceState3.replace(' ','').capitalize()}${index}"; state.speechDevice = getCombinedSpeechDevices(deviceType, index); myVolume = getDesiredVolume(settings."${deviceType}Volume${index}")
        if (!(state?.TalkPhrase == null)) {sendTalk(app.label,state.TalkPhrase, state.speechDevice, myVolume, resume, personality, myVoice, evt)} else {LOGDEBUG("Not configured to speak for this event (${evt.value} <> ${deviceState3})", true)}
    }
	if (evt.value == deviceState4) {
        state.TalkPhrase = settings."${deviceType}TalkOn${deviceState4.replace(' ','').capitalize()}${index}"; state.speechDevice = getCombinedSpeechDevices(deviceType, index); myVolume = getDesiredVolume(settings."${deviceType}Volume${index}")
        if (!(state?.TalkPhrase == null)) {sendTalk(app.label,state.TalkPhrase, state.speechDevice, myVolume, resume, personality, myVoice, evt)} else {LOGDEBUG("Not configured to speak for this event (${evt.value} <> ${deviceState3})", true)}
    }
    state.TalkPhrase = null
    state.speechDevice = null
}

// BEGIN DELAYED / REPEATED ANNOUNCEMENTS
//Transforms a device state value ("fan only") into the settings-key fragment ("Fanonly"), mirroring the TalkOn phrase key logic
def delayStateKeyPart(deviceState){
    return deviceState?.toString()?.replace(' ','')?.capitalize()
}
def delayToSeconds(amount, unit){
    def a = 0
    try { if (amount != null && amount.toString().isNumber()) { a = amount.toDouble() } } catch (ex) { a = 0 }
    if (a <= 0) { return 0 }
    if (unit == "Seconds") { return Math.max(1, (int)Math.ceil(a)) }
    if (unit == "Hours") { return Math.max(1, (int)Math.ceil(a * 3600)) }
    return Math.max(1, (int)Math.ceil(a * 60)) //Minutes (default)
}
def getDelaySeconds(deviceType, deviceState, index){
    def part = delayStateKeyPart(deviceState)
    return delayToSeconds(settings?."${deviceType}${part}DelayAmount${index}", settings?."${deviceType}${part}DelayUnit${index}")
}
def getRepeatSeconds(deviceType, deviceState, index){
    def part = delayStateKeyPart(deviceState)
    return delayToSeconds(settings?."${deviceType}${part}RepeatEveryAmount${index}", settings?."${deviceType}${part}RepeatEveryUnit${index}")
}
def getRepeatLimit(deviceType, deviceState, index){
    def part = delayStateKeyPart(deviceState)
    def lim = settings?."${deviceType}${part}RepeatLimit${index}"
    try { if (lim != null && lim.toString().isNumber() && lim.toDouble() > 0) { return lim.toInteger() } } catch (ex) {}
    return null //blank = unlimited
}
//Queue an announcement to fire after delaySec; on fire the device state is rechecked and the announcement only speaks if the state still holds.
//repeatSec > 0 reschedules a recheck every repeatSec while the state persists. alreadyAnnounced seeds the announcement counter (used when the first announcement went out immediately).
def queueDelayedAnnounce(deviceType, index, deviceStates, targetState, evt, delaySec, repeatSec, alreadyAnnounced){
    def q = state.delayQueue ?: []
    def deviceId = null
    try { deviceId = evt?.deviceId?.toString() } catch (ex) {}
    if (deviceId == null) { try { deviceId = evt?.device?.id?.toString() } catch (ex2) {} }
    def attrName = null
    try { attrName = evt?.name?.toString() } catch (ex3) {}
    def displayName = ""
    try { displayName = (evt?.displayName ?: evt?.device?.displayName ?: "")?.toString() } catch (ex4) {}
    def descText = ""
    try { descText = (evt?.descriptionText ?: "")?.toString() } catch (ex5) {}
    //Newest event for the same device + attribute + state wins; drop older pending entries for it
    q = q.findAll { entry -> !(entry.deviceId == deviceId && entry.attrName == attrName && entry.targetState == targetState?.toString()) }
    def entry = [deviceId: deviceId, attrName: attrName, targetState: targetState?.toString(),
        deviceType: deviceType?.toString(), index: index,
        state1: deviceStates.size() > 0 ? deviceStates[0]?.toString() : "", state2: deviceStates.size() > 1 ? deviceStates[1]?.toString() : "",
        state3: deviceStates.size() > 2 ? deviceStates[2]?.toString() : "", state4: deviceStates.size() > 3 ? deviceStates[3]?.toString() : "",
        fireAt: now() + (delaySec * 1000L), announced: alreadyAnnounced ?: 0,
        repeatSec: repeatSec ?: 0, repeatLimit: getRepeatLimit(deviceType, targetState, index),
        evtName: attrName, evtDisplayName: displayName, evtDescriptionText: descText]
    q << entry
    state.delayQueue = q
    LOGDEBUG("Delayed announcement queued: ${deviceType} still '${targetState}' in ${delaySec}s${repeatSec > 0 ? ", repeating every ${repeatSec}s" : ""} (device ${deviceId})", true)
    runIn(delaySec < 1 ? 1 : (int)delaySec, "processDelayQueue", [overwrite: false])
}
//Scheduled entry point: announce due entries whose device is still in the target state, reschedule repeats, drop the rest
def processDelayQueue(){
    def q = state.delayQueue ?: []
    if (q.isEmpty()) { return }
    def nowMs = now()
    def waiting = []
    def due = []
    q.each { entry ->
        if (entry.fireAt == null || entry.fireAt <= nowMs) { due << entry } else { waiting << entry }
    }
    due.each { entry -> processDelayEntry(entry, waiting) }
    state.delayQueue = waiting
    //Schedule the next check for the earliest remaining entry
    def nextFire = null
    waiting.each { entry -> if (entry.fireAt != null && (nextFire == null || entry.fireAt < nextFire)) { nextFire = entry.fireAt } }
    if (nextFire != null) {
        def secs = Math.max(1, (int)Math.ceil((nextFire - now()) / 1000))
        runIn(secs, "processDelayQueue", [overwrite: false])
    }
}
def processDelayEntry(entry, waiting){
    def deviceType = entry.deviceType
    def index = entry.index ?: 1
    def devices = settings?."${deviceType}DeviceGroup${index}"
    if (devices == null) { devices = settings?."${deviceType}Group${index}" } //filterStatus uses a shorter group setting name
    def dev = devices?.find { d -> d?.id?.toString() == entry.deviceId }
    def cur = null
    if (dev != null && entry.attrName) {
        try {
            def canonAttr = dev.supportedAttributes?.find { a -> a?.name?.equalsIgnoreCase(entry.attrName) }?.name ?: entry.attrName
            cur = dev.currentValue(canonAttr)?.toString()
        } catch (ex) { LOGDEBUG("Delayed recheck failed reading ${entry.attrName}: ${ex}", true) }
    }
    if (cur == null || cur != entry.targetState) {
        LOGDEBUG("Delayed announcement skipped: ${deviceType} is now '${cur}' (was waiting for '${entry.targetState}')", true)
        return //state changed; drop the entry
    }
    //State still holds; restrictions are re-evaluated at announce time
    if (!(processRestrictions("${deviceType}", index))) {
        LOGDEBUG("Delayed announcement skipped by restrictions: ${deviceType} still '${cur}'", true)
        return //drop; a future event will re-queue if still relevant
    }
    def evt = [name: entry.evtName, value: entry.targetState, displayName: entry.evtDisplayName, descriptionText: entry.evtDescriptionText, deviceId: entry.deviceId, isStateChange: true]
    def myVoice = getMyVoice(settings?."${deviceType}Voice${index}")
    announceEvent(deviceType, index, [entry.state1, entry.state2, entry.state3, entry.state4], evt, myVoice)
    def announced = (entry.announced ?: 0) + 1
    def repeatSec = entry.repeatSec ?: 0
    def repeatLimit = entry.repeatLimit
    if (repeatSec > 0 && (repeatLimit == null || announced < repeatLimit)) {
        def next = [:] + entry
        next.announced = announced
        next.fireAt = now() + (repeatSec * 1000L)
        waiting << next
        LOGDEBUG("Repeat announcement ${announced}${repeatLimit ? " of ${repeatLimit}" : ""} scheduled in ${repeatSec}s: ${deviceType} still '${cur}'", true)
    } else if (repeatSec > 0) {
        LOGDEBUG("Repeat limit reached (${announced}); no further announcements for ${deviceType} '${cur}'", true)
    }
}
// END DELAYED / REPEATED ANNOUNCEMENTS

///////////////////////////////////////
///////////////////////////////////////
// SPECIAL DEVICES / PROCESSING      //
///////////////////////////////////////
///////////////////////////////////////

//BEGIN MODE CHANGE
def onModeChangeEvent(evt){
    processModeChangeEvent(1, evt)
}
def processModeChangeEvent(index, evt){
	def resume = ""; resume = parent.returnVar("resumePlay"); if (resume == "") { resume = true }
    def personality = ""; personality = parent.returnVar("personalityMode"); if (personality == "" || personality == null) { personality = false }
    def myVolume = -1
    def myVoice = getMyVoice(settings?.modeVoice1)
    LOGDEBUG("(onModeEvent): Last Mode: ${state.lastMode}, New Mode: ${location.mode}, ${myVoice}", true)
	//LOGDEBUG("(onModeEvent): Restricted FROM modes: ${settings.modeExcludePhraseGroup1}",true)
	//LOGDEBUG("(onModeEvent): FROM restriction test: ${(settings.modeExcludePhraseGroup1.contains(state.lastMode))},${(!(settings.modeExcludePhraseGroup1 == null))}",true)
    //Check Restrictions
    if (!(processRestrictions("mode",index))){ return }
	if (true) {
		if (index == 1) {
			if (!(settings?.modePhraseResumePlay1 == null)) { resume = settings.modePhraseResumePlay1 }
		}
        if (resume == null) { resume = true }
	} else { resume = false }
    if (settings?.modePersonality1 == "Yes") {personality = true}
    if (settings?.modePersonality1 == "No") {personality = false}
    if (settings?.modePhraseGroup1?.contains(location.mode)){
        if (!(settings.modeExcludePhraseGroup1 == null)){
            //settings.modeExcluePhraseGroup1 is not empty
            if ((settings.modeExcludePhraseGroup1.contains(state.lastMode)) == false) {
				//LOGDEBUG("onModeEvent(): Exclusion modes exist but we are not coming from one of them",true)
                //If we are not coming from an exclude mode, Talk.
                state.TalkPhrase = null
                state.speechDevice = null
                state.TalkPhrase = settings.modeTalkOnChange1; state.speechDevice = getCombinedSpeechDevices("modePhrase", 1); myVolume = getDesiredVolume(settings.modePhraseVolume1)
                if (!(state?.TalkPhrase == null)) {sendTalk(app.label,state.TalkPhrase, state.speechDevice, myVolume, resume, personality, myVoice, evt)} else {LOGDEBUG("Not configured to speak for this event", true)}
                state.TalkPhrase = null
                state.speechDevice = null
            } else {
                LOGDEBUG("Mode change silent due to exclusion configuration (${state.lastMode} >> ${location.mode})", true)
            }
        } else {
            //settings.modeExcluePhraseGroup1 is empty, no exclusions, Talk.
			//LOGDEBUG("onModeEvent(): Exclusion modes do not exist",true)
            state.TalkPhrase = null
            state.speechDevice = null
            state.TalkPhrase = settings.modeTalkOnChange1; state.speechDevice = getCombinedSpeechDevices("modePhrase", 1); myVolume = getDesiredVolume(settings.modePhraseVolume1)
            if (!(state?.TalkPhrase == null)) {sendTalk(app.label,state.TalkPhrase, state.speechDevice, myVolume, resume, personality, myVoice, evt)} else {LOGDEBUG("Not configured to speak for this event", true)}
            state.TalkPhrase = null
            state.speechDevice = null
        }
    }
	state.lastMode = location.mode
}
//END MODE CHANGE


//BEGIN HANDLE ENERGY METER
def onPowerMeter1Event(evt){
    processPowerMeterEvent(1, evt)
}

def processPowerMeterEvent(index, evt){
	def resume = ""; resume = parent.returnVar("resumePlay"); if (resume == "") { resume = true }
    def personality = ""; personality = parent.returnVar("personalityMode"); if (personality == "" || personality == null) { personality = false }
    def myVolume = -1
    def myVoice = getMyVoice(settings?.buttonVoice1)
    def energySpeak = false
    def powerLevel = 0
    def deviceName = ""
    try {
		deviceName = evt.displayName  //User given name of the device triggering the event
    } catch (ex) { 
    	LOGDEBUG("processPowerMeterEvent() evt.displayName failed; trying evt.device.displayName", false)
        try {
        	deviceName = evt.device.displayName //User given name of the device triggering the event
		} catch (ex2) {
			LOGDEBUG("processPowerMeterEvent() evt.device.displayName filed; trying evt.device.name")
			try {
				deviceName = evt.device.name //Platform name for the device triggering the event
			} catch (ex3) {
				LOGDEBUG("processPowerMeterEvent() evt.device.name filed; Giving up")
				deviceName = "Unknown"
			}
		}
	}
    //powerLevel = Math.round(evt.value.toDouble()).toString()
    try {
    	powerLevel = evt?.value?.toDouble()?.trunc()?.toString()?.replace(".0","")
    } catch (err) {
    	powerLevel = evt?.value
    }
    LOGDEBUG("(onPowerMeterEvent): ${evt.name}, ${index}, ${evt.value}, ${powerLevel}, ${myVoice}", true)
	//Check Restrictions
    if (!(processRestrictions("powerMeter",index))){ return }
    state.TalkPhrase = null
    state.speechDevice = null
	if (true) {
		if (index == 1) {
			if (!(settings?.powerMeterResumePlay1 == null)) { resume = settings.powerMeterResumePlay1 }
		}
        if (resume == null) { resume = true }
	} else { resume = false }
    if (settings?.powerMeterPersonality1 == "Yes") {personality = true}
    if (settings?.powerMeterPersonality1 == "No") {personality = false}
    if (!(state?.powerMeterState.contains("|${deviceName}-"))) {
    	state.powerMeterState = "${state.powerMeterState}|${deviceName}-UNKNOWN|"
    }
    def pwr = 0d
    try { pwr = powerLevel.toString().toDouble() } catch (ex) { pwr = 0d }
    def riseT = null
    def fallT = null
    try { def v = settings?.powerMeterTalkOnRiseThold1; if (v != null && v != "") { riseT = v.toString().toDouble() } } catch (ex) { riseT = null }
    try { def v = settings?.powerMeterTalkOnFallThold1; if (v != null && v != "") { fallT = v.toString().toDouble() } } catch (ex) { fallT = null }
    //HIGH
    if (riseT != null && pwr > riseT) { 
    	if ((!(state?.powerMeterState?.contains("|${deviceName}-HIGH|"))) && energySpeak == false) { 
        	state.powerMeterState = state.powerMeterState.replace("|${deviceName}-UNKNOWN|","|${deviceName}-HIGH|")
            state.powerMeterState = state.powerMeterState.replace("|${deviceName}-NORMAL|","|${deviceName}-HIGH|")
            state.powerMeterState = state.powerMeterState.replace("|${deviceName}-LOW|","|${deviceName}-HIGH|")
            energySpeak = true 
        }
    }
    //NORMAL
    if (((pwr > fallT || fallT == null || fallT == 0) && (riseT != null && pwr < riseT))) { 
    	if ((!(state?.powerMeterState?.contains("|${deviceName}-NORMAL|"))) && energySpeak == false) { 
        	state.powerMeterState = state.powerMeterState.replace("|${deviceName}-UNKNOWN|","|${deviceName}-NORMAL|")
            state.powerMeterState = state.powerMeterState.replace("|${deviceName}-LOW|","|${deviceName}-NORMAL|")
            state.powerMeterState = state.powerMeterState.replace("|${deviceName}-HIGH|","|${deviceName}-NORMAL|")
            energySpeak = true 
        }
        if (fallT == null || fallT == 0) { 
        	// If Low = 0, Then override LOW alert, we've returned to Normal (can't go lower).
        	state.powerMeterState = state.powerMeterState.replace("|${deviceName}-UNKNOWN|","|${deviceName}-NORMAL|")
            state.powerMeterState = state.powerMeterState.replace("|${deviceName}-HIGH|","|${deviceName}-NORMAL|")
        }
    }
    //LOW
    if ((fallT != null && pwr < fallT) && fallT > 0) { 
    	if ((!(state?.powerMeterState?.contains("|${deviceName}-LOW|"))) && energySpeak == false) { 
        	state.powerMeterState = state.powerMeterState.replace("|${deviceName}-UNKNOWN|","|${deviceName}-LOW|")
            state.powerMeterState = state.powerMeterState.replace("|${deviceName}-NORMAL|","|${deviceName}-LOW|")
            state.powerMeterState = state.powerMeterState.replace("|${deviceName}-HIGH|","|${deviceName}-LOW|")
            energySpeak = true 
        }
    }
    if (index == 1 && state.powerMeterState.contains("|${deviceName}-HIGH|") && energySpeak) {state.TalkPhrase = settings.powerMeterTalkOnRise1; state.speechDevice = getCombinedSpeechDevices("powerMeter", 1); myVolume = getDesiredVolume(settings.powerMeterVolume1)}
    if (index == 1 && state.powerMeterState.contains("|${deviceName}-NORMAL|") && energySpeak) {state.TalkPhrase = settings.powerMeterTalkOnNormal1; state.speechDevice = getCombinedSpeechDevices("powerMeter", 1); myVolume = getDesiredVolume(settings.powerMeterVolume1)}
    if (index == 1 && state.powerMeterState.contains("|${deviceName}-LOW|") && energySpeak) {state.TalkPhrase = settings.powerMeterTalkOnFall1; state.speechDevice = getCombinedSpeechDevices("powerMeter", 1); myVolume = getDesiredVolume(settings.powerMeterVolume1)}
    LOGDEBUG("energySpeak=${energySpeak}, powerLevel=${powerLevel}, state.powerMeterState=${state.powerMeterState}", false)
    if (!(state?.TalkPhrase == null)) {
    	if (state.TalkPhrase.toLowerCase().contains("%value%")) {
    		state.TalkPhrase = state.TalkPhrase.replaceAll(/(?i)\%value\%/, powerLevel)
    	}
    	sendTalk(app.label,state.TalkPhrase, state.speechDevice, myVolume, resume, personality, myVoice, evt)
    } else {
    	LOGDEBUG("Not configured to speak for this event", true)
    }
    state.TalkPhrase = null
    state.speechDevice = null
}
//END HANDLE ENERGY METER


//BEGIN HANDLE TIME SCHEDULE
def onSchedule1Event(){
    processScheduledEvent(1, timeSlotTime1, timeSlotDays1)
}
def onSchedule2Event(){
    processScheduledEvent(2, timeSlotTime2, timeSlotDays2)
}
def onSchedule3Event(){
    processScheduledEvent(3, timeSlotTime3, timeSlotDays3)
}

def processScheduledEvent(index, eventtime, alloweddays){
	
	def resume = ""; resume = parent.returnVar("resumePlay")
    if (resume == "" || resume == null) { resume = false }
    def personality = ""; personality = parent.returnVar("personalityMode"); if (personality == "" || personality == null) { personality = false }
    def timeNow = getTimeFromCalendar(false, true)
    //def personality = false; personality = parent.returnVar("personalityMode
    //if (personality == "" || personality == null) { personality = false }
    def myVolume = -1
    def myVoice = ""
    if (index == 1) {myVoice = getMyVoice(settings?.timeSlotVoice1)}
    if (index == 2) {myVoice = getMyVoice(settings?.timeSlotVoice2)}
    if (index == 3) {myVoice = getMyVoice(settings?.timeSlotVoice3)}
    LOGDEBUG("(onScheduledEvent): ${timeNow}, ${index}, ${myVoice}", true)
    //Check Restrictions
    if (!(processRestrictions("timeSlot",index))){ return }
	if (true) {
		if (index == 1){if (!(settings?.timeSlotResumePlay1 == null)) { resume = settings.timeSlotResumePlay1 }}
		if (index == 2){if (!(settings?.timeSlotResumePlay2 == null)) { resume = settings.timeSlotResumePlay2 }}
		if (index == 3){if (!(settings?.timeSlotResumePlay3 == null)) { resume = settings.timeSlotResumePlay3 }}
	}
	if (resume == null) { resume = true } else { resume = false }
    if (index == 1 && settings?.timeSlotPersonality1 == "Yes") {personality = true}
    if (index == 1 && settings?.timeSlotPersonality1 == "No") {personality = false}
	if (index == 2 && settings?.timeSlotPersonality2 == "Yes") {personality = true}
    if (index == 2 && settings?.timeSlotPersonality2 == "No") {personality = false}
    if (index == 3 && settings?.timeSlotPersonality3 == "Yes") {personality = true}
    if (index == 3 && settings?.timeSlotPersonality3 == "No") {personality = false}  
	if (index == 1) { state.TalkPhrase = settings.timeSlotTalkOnTime1; state.speechDevice = getCombinedSpeechDevices("timeSlot", 1); myVolume = getDesiredVolume(settings.timeSlotVolume1) }
	if (index == 2) { state.TalkPhrase = settings.timeSlotTalkOnTime2; state.speechDevice = getCombinedSpeechDevices("timeSlot", 2); myVolume = getDesiredVolume(settings.timeSlotVolume2) }
	if (index == 3) { state.TalkPhrase = settings.timeSlotTalkOnTime3; state.speechDevice = getCombinedSpeechDevices("timeSlot", 3); myVolume = getDesiredVolume(settings.timeSlotVolume3) }
	def customevent = [displayName: 'BigTalker:OnSchedule', name: 'OnSchedule', value: "${todayStr}@${timeNow}"]
	sendTalk(app.label,state.TalkPhrase, state.speechDevice, myVolume,resume, personality, myVoice, customevent)
	state.TalkPhrase = null
    state.speechDevice = null
}
//END HANDLE TIME SCHEDULE

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
def initFormattingVars(){
	state.formatSettingRootStart = parent.returnVar("formatSettingRootStart")
	state.formatSettingRootEnd = parent.returnVar("formatSettingRootEnd")
	state.formatSettingOptionalStart = parent.returnVar("formatSettingOptionalStart")
	state.formatSettingOptionalEnd = parent.returnVar("formatSettingOptionalEnd")
    state.formatUlStart = parent.returnVar("formatUlStart")
    state.formatUlEnd = parent.returnVar("formatUlEnd")
    state.formatLiStart = parent.returnVar("formatLiStart")
	state.formatLiEnd = parent.returnVar("formatLiEnd")
    state.formatIStart = parent.returnVar("formatIStart")
	state.formatIEnd = parent.returnVar("formatIEnd")
    state.formatStrongStart = parent.returnVar("formatStrongStart")
	state.formatStrongEnd = parent.returnVar("formatStrongEnd")
    state.formatHr = parent.returnVar("formatHr")
	state.formatBr = parent.returnVar("formatBr")
    state.formatCenterStart = parent.returnVar("formatCenterStart")
	state.formatCenterEnd = parent.returnVar("formatCenterEnd")
}

def getDesiredVolume(invol) {
	def globalVolume = parent.returnVar("speechVolume")
    def globalMinimumVolume = parent.returnVar("speechMinimumVolume")
    def myVolume = invol
    def finalVolume = -1
    if (myVolume > 0) { 
    	finalVolume = myVolume
	} else {
		if (globalVolume > 0) {
			finalVolume = globalVolume
		} else {
            if (globalMinimumVolume > 0) {
                finalVolume = globalMinimumVolume
            } else {
                finalVolume = 50 //Default if no volume parameters are set
            }
        }
	}
    if (true) { 
    	LOGDEBUG("finalVolume: ${finalVolume}", true)
    }
    return finalVolume
}

def getMyVoice(deviceVoice){
    def myVoice = "Not Used"
    if (!(deviceVoice == null )) { myVoice = deviceVoice }
    if (true) {
    	LOGDEBUG("getMyVoice: deviceVoice=${myVoice}",false)
        LOGDEBUG("getMyVoice: settings.parent.speechVoice=${parent.returnVar("speechVoice")}",false)
		myVoice = (!(deviceVoice == null || deviceVoice == "")) ? deviceVoice : (parent.returnVar("speechVoice") ? parent.returnVar("speechVoice") : "Salli(en-us)")
    }
    return myVoice
}

//Escape user/remote-supplied text for safe rendering inside HTML
def escapeHtml(sText) {
	if (sText == null) { return "" }
	def s = sText.toString()
	s = s.replaceAll(/&/, '&amp;')
	s = s.replaceAll(/</, '&lt;')
	s = s.replaceAll(/>/, '&gt;')
	s = s.replaceAll(/"/, '&quot;')
	s = s.replaceAll(/'/, '&#39;')
	return s
}

def getCombinedSpeechDevices(devicePrefix, index = 1){
    //Gather devices from the three friendly pickers (music player, speech, audio notification)
    def devs = []
    if (settings."${devicePrefix}MusicPlayerDevice${index}") { devs = devs + settings."${devicePrefix}MusicPlayerDevice${index}" }
    if (settings."${devicePrefix}SpeechSynthesisDevice${index}") { devs = devs + settings."${devicePrefix}SpeechSynthesisDevice${index}" }
    if (settings."${devicePrefix}AudioNotificationDevice${index}") { devs = devs + settings."${devicePrefix}AudioNotificationDevice${index}" }
    return devs
}

def sendTalk(appname, phrase, customSpeechDevice, volume, resume, personality, voice, evt, mode = "auto"){
    def talkMode = "auto"
    if (mode != "" && mode != null) { talkMode = mode }
    LOGDEBUG("parent.Talk(app=Me,customdevice=${customSpeechDevice},volume=${volume},resume=${resume},personality=${personality},voice=${voice},mode=${talkMode},evt=${evt},phrase=${phrase})", false)
	parent.Talk(appname, phrase, customSpeechDevice, volume, resume, personality, voice, evt, talkMode)
}

def LOGDEBUG(txt, send){
	if (txt == null) { 
		log.debug "Debug log data was null"
		if (send == true) { parent.LOGDEBUG("[CHILD:${app.label}] Debug log data was null") }
	}
	if (send == true || send == null || send == "") { def sendToParent = true } else { def sendToParent = false }
    txt = txt?.toString()?.replaceAll(/[\r\n]+/, ' ')
	if (parent?.returnVar("debugMode") && sendToParent) {parent.LOGDEBUG("[CHILD:${app?.label}] ${txt}")}
    try {
    	if (parent?.returnVar("debugMode") || sendToParent == false) { log.debug("BigTalker3-CHILD[${app?.label?.replace(" ","").toUpperCase()}](${state.version}) || ${txt}")}
    } catch(ex) {
		log.error("LOGDEBUG unable to output requested data! || ${txt} || ERR(${ex})")
    }
}
def LOGTRACE(txt){
    txt = txt?.toString()?.replaceAll(/[\r\n]+/, ' ')
	parent.LOGTRACE("[CHILD:${app.label}] ${txt}")
    try {
    	log.trace("BigTalker3-CHILD[${app.label.replace(" ","").toUpperCase()}](${state.version}) || ${txt}")
    } catch(ex) {
    	log.error("LOGTRACE unable to output requested data!")
    }
}
def LOGERROR(txt){
    txt = txt?.toString()?.replaceAll(/[\r\n]+/, ' ')
	parent.LOGERROR("[CHILD:${app.label}] ${txt}")
    try {
    log.error("BigTalker3-CHILD[${app.label.replace(" ","").toUpperCase()}](${state.version}) || ERROR: ${txt}")
    } catch(ex) {
    	log.error("LOGERROR unable to output requested data!")
    }
}

def version(){
    resetBtnName()
	//schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
    //checkButtons()
    //pauseOrNot()
    
}

def checkButtons(){
    LOGDEBUG("Running checkButtons",false)
    appButtonHandler("updateBtn")
}


def appButtonHandler(btn){
    state.btnCall = btn
    if(state.btnCall == "updateBtn"){
       log.info "Checking for updates now..."
        updateCheck()
        pause(3000)
  		state.btnName = state.versionBtn
        runIn(2, resetBtnName)
    }
    if(state.btnCall == "updateBtn1"){
    state.btnName = "Update Available - Click Here" 
    //httpGet("https://github.com/CobraVmax/Hubitat/tree/master/Apps' target='_blank")
    }
    
}   
def resetBtnName(){
//    log.info "Resetting Update Button Name"
	if(state.versionStatus != "Current"){
		state.btnName = state.versionBtn
	}
	else{
		state.btnName = "Check For Update" 
	}
}    

def displayVersionStatus(){
	//Cobra update code, modified by Rayzurbock
    def versionInfo = ""
	versionInfo = "${state.formatHr}${state.formatHr}${state.formatStrongStart}${state.formatCenterStart}Version Information${state.formatCenterEnd}${state.formatStrongEnd}${state.formatBr}"
	if(state.versionStatus){
    	if (state.hubType == "Hubitat") {
			versionInfo += "<img src='https://lowrance.cc/ST/icons/BigTalker-CurrentVersion.png'</img>${state.formatBr}${state.ExternalName} - Version: ${state.version} ${state.formatBr}<font face='Lucida Handwriting'>${state.Copyright ?: ""} </font>"
		}
    }
	if((state.versionStatus != "${state.formatStrongStart}** This app is no longer supported by $state.author  **${state.formatStrongEnd}") & (state.versionStatus != "Current")){
		input "updateBtn", "button", title: "${state.btnName}"
    
		//  section(){
		//		log.info "app.label = $app.label"
		//		input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false  
		//	}
		//	pauseOrNot()   
			//if(state.versionStatus != "Current"){
		versionInfo += "${state.formatStrongStart}${escapeHtml(state.versionStatus ?: "")}${state.formatStrongEnd}${state.formatBr}${escapeHtml(state.updateURI ?: "")}${state.formatBr}${state.formatStrongStart}${state.formatIStart}Release Notes:${state.formatIEnd}${state.formatStrongEnd}${escapeHtml(state.UpdateInfo ?: "")}${state.formatBr}"
			//}
		//section(" ") {
		//	input "updateNotification", "bool", title: "Send a 'Pushover' message when an update is available", required: true, defaultValue: false, submitOnChange: true 
		//	if(updateNotification == true){ input "speaker", "capability.speechSynthesis", title: "PushOver Device", required: true, multiple: true}
		//}
	} else {
		versionInfo += "${state.formatIStart}App is up to date${state.formatIEnd}"
	}
    paragraph versionInfo
}

def updateCheck(){
	setVersion()
	def lastKnownVersionStatus = state.versionStatus
	if (state?.versionStatus == null) { state.versionStatus = "${state.formatIStart}Unknown${state.formatIEnd}" }
	def paramsUD = [uri: "https://lowrance.cc/ST/manifests/RayzurCodeHE.json"]
	if (updateCheckAllowed() || lastKnownVersionStatus == "${state.formatIStart}Unknown${state.formatIEnd}" || lastKnownVersionStatus == null){
		state.Copyright = ""
		state.updateURI = ""
		state.UpdateInfo = ""
		state.author = ""
		state.versionBtn = ""
		def newVerRaw = ""
		def newVer = ""
		def currentVer = ""
		try {
			httpGet(paramsUD) { respUD ->
 			  //log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
				def copyrightRead = (respUD.data.copyright)
				//Manifest content is our own; keep any HTML entity intact for display and roll the copyright year forward
				state.Copyright = copyrightRead?.replaceFirst(/\b(19|20)\d{2}\b/, new Date().format("yyyy"))
				def updateUri = (respUD.data.versions.UpdateInfo.GithubFiles.(state.InternalName))
				state.updateURI = updateUri   
				newVerRaw = (respUD.data.versions.Application.(state.InternalName))
				if (newVerRaw == null) {
					//Not listed in the update manifest (e.g. DEV builds); nothing to compare against
					state.versionStatus = "Current"
					log.info "updateCheck(): ${state.InternalName} not found in update manifest; skipping version comparison"
					return
				}
				newVer = (newVerRaw.replace(".", ""))
				currentVer = state.version.replace(".", "")
				state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
				state.author = (respUD.data.author)
				log.debug "currentVer=${currentVer}, newVer=${newVer}"
				if(newVer == "NLS"){
					state.versionStatus = "${state.formatStrongStart}** This app is no longer supported by ${state.author}  **${state.formatStrongEnd}"  
					log.warn "** This app is no longer supported by ${state.author} **" 
				}
				else if(currentVer < newVer){
					state.versionStatus = "New Version Available (Version: ${newVerRaw})"
					log.warn "** There is a newer version of this app available  (Version: ${newVerRaw}) **"
					log.warn "** ${escapeHtml(state.UpdateInfo ?: "")} **"
					state.versionBtn = "UPDATE AVAILABLE"
					def updateMsg = "There is a new version of '${state.ExternalName}' available (Version: ${newVerRaw})"
					//pushOverNow(updateMsg)
				} 
				else{ 
					state.versionStatus = "Current"
					log.info "You are using the current version of this app"
				}
			}
		} catch (e) {
			log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
		}
		if(state.versionStatus != "Current"){
			state.versionBtn = "UPDATE AVAILABLE"
		}
		else{
			state.versionBtn = "No Update Available"
		}
	} else {
		return
	}
}

def updateCheckAllowed(){
	// rayzurbock code, used with Cobra update code
	def proceed = false
	def updateCheckCurrentDate = new Date().getTime()
	def updateCheckIntervalInMil = (state.updateActiveUseIntervalMin * 60000)
	if (state?.updateNextCheckDate == null) { state.updateNextCheckDate = new Date().getTime() }
	def timeDiff = state?.updateNextCheckDate - updateCheckCurrentDate
	if (timeDiff <= 0 || timeDiff == null) { proceed = true }
	if (state?.updateNextCheckDate == null) { proceed = true }
	//if (!(proceed == true)) { log.debug "updateCheckAllowed() result: not allowed to proceed"}
	LOGDEBUG("updateCheckAllowed() timeDiff=${timeDiff}, proceed=${proceed}", false)
	if (proceed) {
		//log.debug("updateCheckAllowed(): result: proceed")
		state.updateNextCheckDate = new Date().getTime() + updateCheckIntervalInMil
		return true
	} else { 
		//log.debug("updateCheckAllowed(): result: do not proceed (${timeDiff})")
		return false
	}
	
}

def setVersion(){
		state.version = "3.0.0-a6"	 
    	state.InternalName = "BigTalker3-Child-DEV" 
		state.ExternalName = "BigTalker3 Child-DEV"
		state.updateActiveUseIntervalMin = 30 //time in minutes to check for updates while using the App
}

