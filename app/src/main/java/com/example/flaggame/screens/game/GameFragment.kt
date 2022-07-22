package com.example.flaggame.screens.game

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.flaggame.Constants
import com.example.flaggame.Question
import com.example.flaggame.R
import com.example.flaggame.databinding.FragmentGameBinding

class GameFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentGameBinding

    private lateinit var mQuestionsList : ArrayList<Question>

    private  var mSelectedPosition: Int = 0
    private var mCorrectAnswer : Int = 0
     var mCurrrentPosition: Int = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)


        // we have created a new arraylist and stored all our question and answer in it
        mQuestionsList = Constants.getQuestion()

        Log.i("info", mCurrrentPosition.toString())


        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)


            setQuestion()






        return binding.root
    }

    private fun setQuestion() {


        val question: Question = mQuestionsList[mCurrrentPosition -1]

        binding.tvQuestion.text = question.question
        binding.imageView.setImageResource(question.image)
        binding.tvOptionOne.text = question.optionOne
        binding.tvOptionTwo.text = question.optionTwo
        binding.tvOptionThree.text = question.optionThree
        binding.tvOptionFour.text = question.optionFour

        binding.pb.progress = mCurrrentPosition
        binding.tvProgress.text = "$mCurrrentPosition" + "/" + binding.pb.max // displays question number

/// this resets the appearance for everytime a new question comes up
        defaultAppearance()


// if all questions in the lists are used

        if (mCurrrentPosition == mQuestionsList.size){

            binding.btnSubmit.text = "Quiz Finished"

        } else {

            binding.btnSubmit.text = "Submit"
        }






    }

    private fun defaultAppearance() {

        //controlling textview that share same behavior

        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)


        for (option in options){


            option.setTextColor(Color.parseColor("#7A8089"))
            //default appearance
            option.typeface = Typeface.DEFAULT
            option.background = context?.let { ContextCompat.getDrawable(it, R.drawable.default_option_border_bg) }


        }



    }

    override fun onClick(v: View?) {

        when(v?.id) {

            R.id.tv_optionOne->{

                selectedOptionView(binding.tvOptionOne, 1)

            }

            R.id.tv_optionTwo->{

                selectedOptionView(binding.tvOptionTwo, 2)



            }

            R.id.tv_optionThree->{

                selectedOptionView(binding.tvOptionThree, 3)



            }

            R.id.tv_optionFour->{

                selectedOptionView(binding.tvOptionFour, 4)




            }

            R.id.btnSubmit->{
                // if user has not selected any option
                if (mSelectedPosition == 0){

                    mCurrrentPosition++ // when current position is increasing means index is increasing
                    //if index is increasing that means we will get a new question from the question list


                    when{
                        mCurrrentPosition <= mQuestionsList.size->{

                            setQuestion()

                        } else -> {

                            // go to result

                             val action = GameFragmentDirections.actionGameFragmentToScoreFragment()
                             val nameOfPlayer by navArgs<GameFragmentArgs>()
                             action.score = mCorrectAnswer
                             action.name = nameOfPlayer.name
                             findNavController().navigate(action)

                        }
                    }
                } else {
                    // if user selected an option
                    // we will check if its correct or incorrect

                    val question = mQuestionsList[mCurrrentPosition-1]

                    // if selected position 1..4 matches value at the correct answer
                    // correct answer values are 1..4 if the value matches or not matches
                    if (question.correctAnswer!=mSelectedPosition) {

                        answerView(mSelectedPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        mCorrectAnswer++


                    }


                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (mCurrrentPosition == mQuestionsList.size){

                        binding.btnSubmit.text = "Finished"
                    } else {

                        binding.btnSubmit.text = " Go to Next Question"
                    }

                    mSelectedPosition = 0 // next question options positions should be 0

                }




            }


        }



    }

    private fun selectedOptionView(tv: TextView, selectedPosition: Int) {
        //reset text views
        defaultAppearance()

        mSelectedPosition = selectedPosition



        tv.setTextColor(Color.parseColor("#363A43"))
        //default appearance
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = context?.let { ContextCompat.getDrawable(it, R.drawable.selected_option_border_bg) }






    }

    private fun answerView(mSelectedPosition: Int, drawableView: Int) {

        when (mSelectedPosition) {

            1->{

                binding.tvOptionOne.background =
                    context?.let { ContextCompat.getDrawable(it, drawableView) }

            }

            2->{

                binding.tvOptionTwo.background =
                    context?.let { ContextCompat.getDrawable(it, drawableView) }


            }

            3->{

                binding.tvOptionThree.background =
                    context?.let { ContextCompat.getDrawable(it, drawableView) }


            }

            4->{

                binding.tvOptionFour.background =
                    context?.let { ContextCompat.getDrawable(it, drawableView) }



            }
        }
    }




}