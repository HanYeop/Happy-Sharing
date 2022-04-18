package com.hanyeop.happysharing

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.hanyeop.happysharing.databinding.ActivityQuizBinding
import com.hanyeop.happysharing.model.QuizDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel
import java.util.*

class QuizActivity : AppCompatActivity() {

    private lateinit var binding : ActivityQuizBinding
    private val args by navArgs<QuizActivityArgs>()

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()

    // 현재 퀴즈
    private var curQuiz = QuizDTO()

    // 현재 유저
    private var user = UserDTO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 난수 생성
        var randomInt = Random().nextInt(Constants.QUIZ_NUMBER)

        // 퀴즈 불러오기
        firebaseViewModel.quizLoad(randomInt)

        // 유저 정보 동기화
        user = args.userDTO

        binding.apply {
            // 퀴즈 바인딩
            firebaseViewModel.currentQuiz.observe(this@QuizActivity){
                questionText.text = it.content
                explainText.text = it.explain
                curQuiz = it
            }

            // O 버튼
            yesButton.setOnClickListener {
                correctText.visibility = View.VISIBLE
                explainText.visibility = View.VISIBLE
                if(curQuiz.correct){
                    correctAnswer()
                }
                else{
                    wrongAnswer()
                }
                // 중복클릭 방지
                yesButton.isEnabled = false
                noButton.isEnabled = false
            }

            // X 버튼
            noButton.setOnClickListener {
                correctText.visibility = View.VISIBLE
                explainText.visibility = View.VISIBLE
                if(!curQuiz.correct){
                   correctAnswer()
                }
                else{
                    wrongAnswer()
                }
                // 중복클릭 방지
                yesButton.isEnabled = false
                noButton.isEnabled = false
            }

            exitButton.setOnClickListener {
                finish()
            }
        }
    }

    // 정답
    private fun correctAnswer(){
        binding.apply {
            correctText.text = "정답입니다!"
            correctText.setTextColor(ColorStateList.valueOf(
                ContextCompat.getColor(
                    this.root.context,
                    R.color.blue
                )
            ))
        }

        // 점수 올려주기
        val userDTO =
            UserDTO(user.uId,user.userId,user.imageUri,user.score+1,user.sharing,user.area)
        firebaseViewModel.profileEdit(userDTO)
    }

    // 오답
    private fun wrongAnswer(){
        binding.apply {
            correctText.text = "오답입니다!"
            correctText.setTextColor(ColorStateList.valueOf(
                ContextCompat.getColor(
                    this.root.context,
                    R.color.red
                )
            ))
        }
    }
}