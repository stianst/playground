const test = require('tape')
const spawn = require('tape-spawn')

test('test encode', t => {
	const st = spawn(t, 'node bin.js encode "Node.js is awesome."')
	st.stdout.match(/Tm9kZS5qcyBpcyBhd2Vzb21lLg/)
	st.end()
})

test('test decode', t => {
	const st = spawn(t, 'node bin.js decode "Tm9kZS5qcyBpcyBhd2Vzb21lLg"')
	st.stdout.match(/Node\.js is awesome\./)
	st.end()
})

test('test escape', t => {
	const st = spawn(t, 'node bin.js escape "This+is/goingto+escape=="')
	st.stdout.match(/This-is_goingto-escape/)
	st.end()
})

test('test decode', t => {
	const st = spawn(t, 'node bin.js unescape "This-is_goingto-escape"')
	st.stdout.match(/This\+is\/goingto\+escape==/)
	st.end()
})

test('test binarydecode', t => {
	const st = spawn(t, 'node bin.js binarydecode "8wUUjd0b" | od -t x1')
	st.stdout.match(/0000000\s+f3\s+05\s+14\s+8d\s+dd\s+1b\s+0000006/)
	st.end()
})
