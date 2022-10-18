package com.othman.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.othman.a7minuteworkout.databinding.ActivityExerciseBinding
import com.othman.a7minuteworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimeDuration: Long = 10
    private var restPauseOffset: Long = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimeDuration: Long = 30
    private var exercisePauseOffset: Long = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var whistlePlayer: MediaPlayer? = null
    private var countPlayer: MediaPlayer? = null
    private var exerciseAdapter : MainAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        tts=TextToSpeech(this, this)
        setSupportActionBar(binding?.tbExercise)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()
        setUpExerciseStatusRecycleView()


        binding?.tbExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        binding?.flRestView?.setOnClickListener {
            if (restTimer != null){
                restTimer!!.cancel()
                restTimer = null
            }else{
                setRestProgressBar()
            }
        }
        binding?.flExercise?.setOnClickListener{
            if (exerciseTimer != null){
                exerciseTimer!!.cancel()
                exerciseTimer = null
            }else{
                setExerciseProgressBar()
            }
        }


        whistlePlayer = setPlayer(R.raw.whistle_sound)
        countPlayer = setPlayer(R.raw.countdown)

        setRestView()

    }

    private fun setPlayer (drawable: Int):MediaPlayer?  {
        try {
            val soundURI =
                Uri.parse("android.resource://com.othman.a7minuteworkout/$drawable")
            val player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            return player
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return null
    }

    private fun customDialogForBackButton() {
        val dialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            dialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setUpExerciseStatusRecycleView() {
        exerciseAdapter = MainAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.layoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }



    override fun onBackPressed() {

        customDialogForBackButton()
        //super.onBackPressed()
    }

    private fun setRestView() {

        //player?.start()

        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExercise?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.upcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()
        //speakOut("next: ${exerciseList!![currentExercisePosition + 1].getName()}")
       // binding?.rvExerciseStatus?.visibility = View.INVISIBLE


        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
            restPauseOffset = 0
        }
        setRestProgressBar()
    }

    private fun setExerciseView() {
        //player?.start()
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExercise?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.upcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE
       // binding?.rvExerciseStatus?.visibility = View.VISIBLE


        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
            exercisePauseOffset = 0
        }

        //speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }


    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress
        restTimer = object: CountDownTimer((restTimeDuration - restPauseOffset) * 1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                restPauseOffset = restTimeDuration - millisUntilFinished/1000
                binding?.progressBar?.max = 10
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
                if (restProgress == 2 ){
                    speakOut("next up: ${exerciseList!![currentExercisePosition + 1].getName()}")
                }
                if (restProgress == 7 ) {
                    countPlayer?.start()
                }
            }


            override fun onFinish() {
                whistlePlayer?.start()
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter?.notifyDataSetChanged()
                setExerciseView()
            }

        }.start()
    }
    private fun setExerciseProgressBar(){
        binding?.pbExercise?.progress = exerciseProgress
        exerciseTimer = object: CountDownTimer((exerciseTimeDuration - exercisePauseOffset)  * 1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                exercisePauseOffset = exerciseTimeDuration - millisUntilFinished/1000
                binding?.pbExercise?.progress = 30 - exerciseProgress
                binding?.tvExerciseTimer?.text = (30 - exerciseProgress).toString()
                if (exerciseProgress == 20 ) {
                    speakOut("10 seconds to go")
                }
                if (exerciseProgress == 28 ) {
                    speakOut("3")
                }
                if (exerciseProgress == 29 ) {
                    speakOut("2")
                }
                if (exerciseProgress == 30 ) {
                    speakOut("1")
                }

            }

            override fun onFinish() {
                whistlePlayer?.start()

                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter?.notifyDataSetChanged()
                if (currentExercisePosition < exerciseList!!.size - 1){
                    setRestView()
                }else{
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }.start()
    }




    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        if (whistlePlayer != null){
            whistlePlayer!!.stop()
        }
        if (countPlayer != null){
            countPlayer!!.stop()
        }


        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {  // TextToSpeech is ready
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }

    }
    private fun speakOut(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


}