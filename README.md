[![Build Status](https://travis-ci.org/gotson/nestor.svg?branch=master)](https://travis-ci.org/gotson/nestor)

# nestor
A private butler service to book yoga classes, running on AWS Lambda.

## History
Nestor is a modern, scalable, serverless automated booking service.

I started the project to automate the bookings of my Yoga classes, I was tired of having to log in every morning at 9am to book classes, and to end up on a waitlist if I was not fast enough.

As the provider doesn't offer any API, I decided to rely on browser automation via [Selenium](https://www.seleniumhq.org/) to perform the bookings.

I wanted the service to run in an unattended manner, so running it on my laptop was out of question. I do have a NAS I could have used to host the service, but the ARM architecture, slow processing power, and flaky home internet connection made me look for alternatives.

I was inspired by the [serverless-chrome](https://github.com/adieuadieu/serverless-chrome) project and tought it could be a good idea to run the whole thing on AWS Lambda.

## Design

The service is split into multiple parts:
* a Cloudwatch Event triggers a Lambda (`nestor-trigger`) every morning at 9am
* `nestor-trigger` looks in DynamoDB for wished classes that matches the booking criterias (2 days in advance by default), and for each matching wished class sends a message via AWS SNS
* each message sent on SNS triggers another Lambda (`nestor-booker`)
* `nestor-booker` starts up headless-chrome via Selenium to perform the booking. The webpage browing and parsing takes quite some time (around a minute), hence being able to perform multiple bookings in parallel allows to respect the maximum execution time of AWS Lambda (5 minutes).

## Thanks

* https://github.com/adieuadieu/serverless-chrome for providing packaged headless-chrome for AWS Lambda
* https://github.com/smithclay/lambdium which gave me pointers on how to use headless-chrome
