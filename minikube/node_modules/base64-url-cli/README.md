# base64-url-cli [![Build Status](https://travis-ci.org/saibotsivad/base64-url-cli.svg?branch=master)](https://travis-ci.org/saibotsivad/base64-url-cli)

This is a thin command line wrapper for the awesome
[base64-url](https://www.npmjs.com/package/base64-url) module.

## Install

	npm install -g base64-url-cli

## Use It

Run it from the command line:

	base64url [encode|decode|escape|unescape|binarydecode] [input]

Encode strings to base64url:

	$ base64url encode "Node.js is awesome."
	Tm9kZS5qcyBpcyBhd2Vzb21lLg

Decode base64url strings:

	$ base64url decode "Tm9kZS5qcyBpcyBhd2Vzb21lLg"
	Node.js is awesome.

Escape strings:

	$ base64url escape "This+is/goingto+escape=="
	This-is_goingto-escape

Unescape strings:

	$ base64url unescape "This-is_goingto-escape"
	This+is/goingto+escape==

Decode base64url to a binary stream:

	$ base64url binarydecode "8wUUjd0b"
	???
	$ base64url binarydecode "8wUUjd0b" | od -t x1
	0000000    f3  05  14  8d  dd  1b                                        
	0000006

## License

[VOL](http://veryopenlicense.com)
