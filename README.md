## Inspiration
Dining in an unfamiliar city can be tough - how will you know what each restaurant has to offer until you walk inside and see the menu? While some restaurants post menus outside, we have offered an even simpler solution: InstantMenu. The InstantMenu app will track your location and can open the menu for a restaurant as soon as you walk by.

## What it does
Instant Menu does what it says on the tin: as soon as you walk into a restaurant, our location tracking services will notify your phone and redirect your browser to the appropriate menu page for that restaurant. Currently, our app is only set up to track one restaurant location, but in the future we would plan to extend this to multiple restaurants and locations.

## How we built it
The app itself is an Android app that integrates with the HyperTrack API to repeatedly update the user’s location and compare it to a target (the bounds of the restaurant). Then, when the app receives the response from the API that it is within the bounds, we automatically open the menu in their browser.

## Challenges we ran into
The HyperTrack API was new to everyone within the group, so we all had to become familiar with it and do further readings into the documentation. We learned a lot about Geofences and location tracking through this API, and in the end we were able to successfully implement it in an Android app.

Originally, we were planning on using Firebase Cloud Functions along with webhooks, in order to send push notifications to devices. However, this was another API that we were unfamiliar with, and given the short timespan of this project we decided it would be quicker to handle the location check on the device itself, and direct the user to a browser immediately rather than have them tap a notification.

## Accomplishments that we're proud of
Our group is very proud of the fact that we were able to come up with a unique idea that leverages an API that was new to us, and were able to successfully implement an app that solves a real world problem, learning lots along the way. We are proud of our ability to work as a team, splitting tasks and supporting each other throughout the process of making this project. We are also proud that we kept a balance between work and fun, and were still able to enjoy many of the amazing workshops, games, and events that Hack the North has to offer.

## What we learned
One main concept that we learned was the HyperTrack API, as this was unfamiliar territory prior to their workshop. We were interested in learning more about this location tracking technology and decided to research problems that we could solve in this space. Along the way, we also gained some experience in Android app development and testing and working collaboratively in team environments. We also learned how to learn, both by researching and examining documentation, looking at examples, and by testing API calls in real code.

## What's next for Instant Menu
Eventually, we would like to build a proper backend server for this service, that handles entering and exiting the geofences using webhooks that call an API which we are planning to write in Flask. This backend server can also support API calls for creating new restaurants, and connect to a database linking restaurants with their corresponding menus and Geofences in the HyperTrack API. We could even add the ability to ask the user to leave a review upon leaving the restaurant. We would also like to allow this app to work in the background rather than needing to be open on the home screen. Finally, we would like to switch to Flutter in order to provide cross-platform mobile support.
