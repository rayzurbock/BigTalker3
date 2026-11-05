# BigTalker3
Have you ever wanted a talking house? Now you can! With the Big Talker SmartApp!

When Hubitat is paired with a compatible audio device (such as a Sonos, Echo, Google Home, LANnouncer, VLCThing, etc) and the Big Talker SmartApp, your house can say what you want it to say when events occur.

BigTalker 3.0.0 is the only supported version. The 2.x line has reached end of life.

## Branches
- `main` (default) — BigTalker 3.0.0 release
- `development` — work in progress toward the next release

## Install
In your Hubitat web interface: **Apps Code** → **New App** → **Import**, using these URLs (parent first, then child), **Save** each, then **Apps** → **Add User App** → **BigTalker3**.

- Parent: https://raw.githubusercontent.com/rayzurbock/BigTalker3/main/smartapps/rayzurbock/bigtalker3.src/bigtalker3.groovy
- Child: https://raw.githubusercontent.com/rayzurbock/BigTalker3/main/smartapps/rayzurbock/bigtalker3-child.src/bigtalker3-child.groovy

Then open the app, select **Configure** to set your defaults, test with **Talk Now**, and create event groups.

## Support
This app is a "community app" and is not supported by Hubitat. Contact [@rayzurbock](https://community.hubitat.com/u/rayzurbock) for support. Use at your own risk.

© 2026 Rayzurbock (Brian S. Lowrance)
