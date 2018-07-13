package jameskjiang.com.sleepreminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

//Used to save the times that are set, and sends message to TimeBroadcastReceive when time is reached
class TimeSaveData(context: Context) {
    var context = context

    //Save data in shared preferences so that broadcast will be called even when phone is rebooted
    var timeSharedRef = context.getSharedPreferences("myref", Context.MODE_PRIVATE)
    fun saveData(hour: Int, min: Int) {
        var editor = timeSharedRef.edit()
        editor.putInt("hour", hour)
        editor.putInt("min", min)
        editor.commit()
    }

    fun getHour(): Int {
        return timeSharedRef!!.getInt("hour", 0)
    }
    fun getMin(): Int {
        return timeSharedRef!!.getInt("min", 0)
    }

    fun setAlarm() {

        val hour = getHour()
        val min = getMin()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar.set(Calendar.SECOND, 0)

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //Send to TimeBroadcastReceiver when time is reached
        var intent = Intent(context, TimeBroadcastReceiver::class.java)
        intent.putExtra("message", "Alarm time")
        intent.action = "com.tester.alarmmanager"
        val pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Repeat everyday at set time
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pi)
    }
}