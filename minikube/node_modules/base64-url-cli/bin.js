#!/usr/bin/env node

const base64 = require('base64-url')

const command = process.argv[2]
const input = process.argv[3]

const commandIsAllowed = typeof base64[command] === 'function'
	|| command === 'binarydecode'

if (!command || !input || !commandIsAllowed) {
	console.log('Missing required parameters, or command is invalid. Run like:')
	console.log('base64url [encode|decode|escape|unescape|binarydecode] [input]')
	console.log('E.g., run this: base64url decode Tm9kZS5qcyBpcyBhd2Vzb21lLg')
} else {
	const output = command === 'binarydecode'
		? Buffer.from(base64.escape(input), 'base64')
		: base64[command](input)
	process.stdout.write(output)
}
