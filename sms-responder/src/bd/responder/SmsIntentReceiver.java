package bd.responder;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsIntentReceiver extends BroadcastReceiver 
{
	private void sendMessage(Context context, Intent intent, SmsMessage inMessage)
	{
		String sendData = "Message Recieved";
		SmsManager mng = SmsManager.getDefault();
		PendingIntent dummyEvent = PendingIntent.getBroadcast(context, 0, new Intent("com.devx.SMSExample.IGNORE_ME"), 0);
		
		String addr = inMessage.getOriginatingAddress();
		
		try{
			mng.sendTextMessage(addr, null, sendData, dummyEvent, dummyEvent);
		}catch(Exception e){
			
		}
	}
	
	private SmsMessage[] getMessagesFromIntent(Intent intent)
	{
		SmsMessage retMsgs[] = null;
		Bundle bdl = intent.getExtras();
		try{
			Object pdus[] = (Object [])bdl.get("pdus");
			retMsgs = new SmsMessage[pdus.length];
			for(int n=0; n < pdus.length; n++)
			{
				byte[] byteData = (byte[])pdus[n];
				retMsgs[n] = SmsMessage.createFromPdu(byteData);
			}	
			
		}catch(Exception e)
		{
			
		}
		return retMsgs;
	}
	public void onReceive(Context context, Intent intent) 
	{
		
		if(!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			return;
		}
		SmsMessage msg[] = getMessagesFromIntent(intent);
		
		for(int i=0; i < msg.length; i++)
		{
			String message = msg[i].getDisplayMessageBody();
			if(message != null && message.length() > 0)
			{
				if(message.startsWith("test"))
				{
					sendMessage(context, intent, msg[i]);
				}
			}
		}
	}
}