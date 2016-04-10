#!/usr/bin/python

import sys, json, xmpp, random, string
import subprocess #for calling the bash script 

SERVER = 'gcm.googleapis.com'
PORT = 5235
USERNAME = '*******'
PASSWORD = '************************'
REGISTRATION_ID = '**************************************************************************'

unacked_messages_quota = 100
send_queue = []

# Return a random alphanumerical id
def random_id():
  rid = ''
  for x in range(8): rid += random.choice(string.ascii_letters + string.digits)
  return rid

def message_callback(session, message):
  global unacked_messages_quota
  gcm = message.getTags('gcm')
  if gcm:
    gcm_json = gcm[0].getData()
    msg = json.loads(gcm_json)
    if not msg.has_key('message_type'):
      # Acknowledge the incoming message immediately.
      send({'to': msg['from'],
            'message_type': 'ack',
            'message_id': msg['message_id']})
      # Queue a response back to the server.
      if msg.has_key('from'):
        print "Has from"
	print msg['from']
	# Send a response back to the app that sent the upstream message.
	send_queue.append({'to': msg['from'],
                           'message_id': random_id(),
                           'data': {'message': "Torrent has been uploaded"}})
	#Output the message to test my syntax
	print "\n the content of this message was "
	print msg['data']['data']
	print "\n"
	#Call the bash script file, making sure the link is in double quotes
	#quotes = '"'
	magnetlink = msg['data']['data'] 
	subprocess.call(["./download_magnet_link.sh", magnetlink])
    elif msg['message_type'] == 'ack' or msg['message_type'] == 'nack':
      unacked_messages_quota += 1

def send(json_dict):
  template = ("<message><gcm xmlns='google:mobile:data'>{1}</gcm></message>")
  client.send(xmpp.protocol.Message(
      node=template.format(client.Bind.bound[0], json.dumps(json_dict)))) 

def flush_queued_messages():
  global unacked_messages_quota
  while len(send_queue) and unacked_messages_quota > 0:
    send(send_queue.pop(0))
    unacked_messages_quota -= 1

client = xmpp.Client('gcm.googleapis.com', debug=['socket'])
client.connect(server=(SERVER,PORT), secure=1, use_srv=False)
auth = client.auth(USERNAME, PASSWORD)
if not auth:
  print 'Authentication failed!'
  sys.exit(1)

client.RegisterHandler('message', message_callback)

while True:
  subprocess.call(["./sleep.sh", "1m"])
  client.Process(1)
  flush_queued_messages()

