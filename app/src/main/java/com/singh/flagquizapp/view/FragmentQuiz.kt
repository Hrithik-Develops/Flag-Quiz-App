package com.singh.flagquizapp.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.singh.flagquizapp.database.DatabaseCopyHelper
import com.singh.flagquizapp.database.FlagsDao
import com.singh.flagquizapp.view.model.FlagsModel
import com.techmania.flagquiz.R
import com.techmania.flagquiz.databinding.FragmentQuizBinding


class FragmentQuiz : Fragment() {


    private lateinit var fragmentQuizBinding: FragmentQuizBinding

    var flagList=ArrayList<FlagsModel>()
    var correctNumber=0
    var wrongNumber=0
    var emptyNumber=0
    var questionNumber=0
    lateinit var correctFlag:FlagsModel
    var wrongFlagList=ArrayList<FlagsModel>()
    val dao= FlagsDao()
    var optionControl=false




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentQuizBinding = FragmentQuizBinding.inflate(inflater, container, false)


        flagList=dao.getRandomTenRecords(DatabaseCopyHelper(requireActivity()) )

        for(flag in flagList){

            Log.d("Flags",flag.id.toString())
            Log.d("Flags",flag.countryName)
            Log.d("Flags",flag.flagName)

        }

        showData()



        fragmentQuizBinding.buttonA.setOnClickListener {
            answerCorrect(fragmentQuizBinding.buttonA)

        }

        fragmentQuizBinding.buttonB.setOnClickListener {
            answerCorrect(fragmentQuizBinding.buttonB)

        }

        fragmentQuizBinding.buttonC.setOnClickListener {
            answerCorrect(fragmentQuizBinding.buttonC)

        }

        fragmentQuizBinding.buttonD.setOnClickListener {
            answerCorrect(fragmentQuizBinding.buttonD)

        }

        fragmentQuizBinding.buttonNext.setOnClickListener {

            questionNumber++
            if (questionNumber > 9){

                if (!optionControl){
                    emptyNumber++
                }

                val direction = FragmentQuizDirections.actionFragmentQuizToFragmentResult().apply {

                    correct = correctNumber
                    wrong = wrongNumber
                    empty = emptyNumber

                }

                this.findNavController().apply {
                    navigate(direction)
                    popBackStack(R.id.fragmentResult,false)
                }


                //Toast.makeText(requireActivity(),"The quiz is finished",Toast.LENGTH_SHORT).show()

            }else{

                showData()

                if (!optionControl){
                    emptyNumber++
                    fragmentQuizBinding.textViewEmptyNumber.text = emptyNumber.toString()
                }else{
                    setButtonToInitialProperty()
                }

            }

            optionControl = false

        }



        // Inflate the layout for this fragment

        return fragmentQuizBinding.root
    }

    fun showData(){

        fragmentQuizBinding.textViewQuestionNumber.text=resources.getString(R.string.question_number).plus(" ").plus(questionNumber +1)
        correctFlag=flagList[questionNumber]
        val flagResourceId = resources.getIdentifier(correctFlag.flagName, "drawable", requireActivity().packageName)
        fragmentQuizBinding.imageViewFlag.setImageResource(flagResourceId)
        wrongFlagList=dao.getRandomThreeRecords(DatabaseCopyHelper(requireActivity()),correctFlag.id)
        val mixOption=HashSet<FlagsModel>()
        mixOption.clear()
        mixOption.add(correctFlag)
        mixOption.add(wrongFlagList[0])
        mixOption.add(wrongFlagList[1])
        mixOption.add(wrongFlagList[2])

        val optionList=ArrayList<FlagsModel>()
        optionList.clear()
        for(eachFlag in mixOption){
            optionList.add(eachFlag)
        }


        fragmentQuizBinding.buttonA.text=optionList[0].countryName
        fragmentQuizBinding.buttonB.text=optionList[1].countryName
        fragmentQuizBinding.buttonC.text=optionList[2].countryName
        fragmentQuizBinding.buttonD.text=optionList[3].countryName


    }

    private fun answerCorrect(button:  Button){
        val clickedOption=button.text.toString()
        val correctAnswer=correctFlag.countryName
        if(clickedOption==correctAnswer){
            correctNumber++
            fragmentQuizBinding.textViewCorrectNumber.text=correctNumber.toString()
            button.setBackgroundColor(Color.GREEN)
        }else {
            wrongNumber++
            fragmentQuizBinding.textViewWrongNumber.text = wrongNumber.toString()
            button.setBackgroundColor(Color.RED)
            button.setTextColor(Color.WHITE)

            when (correctAnswer) {
                fragmentQuizBinding.buttonA.text.toString() -> fragmentQuizBinding.buttonA.setBackgroundColor(
                    Color.GREEN
                )

                fragmentQuizBinding.buttonB.text.toString() -> fragmentQuizBinding.buttonB.setBackgroundColor(
                    Color.GREEN
                )

                fragmentQuizBinding.buttonC.text.toString() -> fragmentQuizBinding.buttonC.setBackgroundColor(
                    Color.GREEN
                )

                fragmentQuizBinding.buttonD.text.toString() -> fragmentQuizBinding.buttonD.setBackgroundColor(
                    Color.GREEN
                )
            }
        }

            fragmentQuizBinding.buttonA.isClickable=false
            fragmentQuizBinding.buttonB.isClickable=false
            fragmentQuizBinding.buttonC.isClickable=false
            fragmentQuizBinding.buttonD.isClickable=false


        optionControl = true

    }

    private fun setButtonToInitialProperty(){
        fragmentQuizBinding.buttonA.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.pink,requireActivity().theme))
            isClickable = true
        }
        fragmentQuizBinding.buttonB.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.pink,requireActivity().theme))
            isClickable = true
        }
        fragmentQuizBinding.buttonC.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.pink,requireActivity().theme))
            isClickable = true
        }
        fragmentQuizBinding.buttonD.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.pink,requireActivity().theme))
            isClickable = true
        }

    }

    }
