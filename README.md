# nestor
A private butler service to book yoga classes.

## History
Nestor is a modern automated booking service.

I started the project to automate the bookings of my Yoga classes, I was tired of having to log in every morning at 9am to book classes, and to end up on a waitlist if I was not fast enough.

As the provider doesn't offer any API, I decided to rely on browser automation via [Selenium](https://www.seleniumhq.org/) to perform the bookings.

Version 1 was running on AWS Lambda.

Version 2 is a single application designed to run on Docker and offer an API as well as a Web UI.
