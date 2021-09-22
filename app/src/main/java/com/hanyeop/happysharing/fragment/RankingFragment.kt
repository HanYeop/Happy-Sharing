package com.hanyeop.happysharing.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.adapter.RankingAdapter
import com.hanyeop.happysharing.databinding.FragmentMoreBinding
import com.hanyeop.happysharing.databinding.FragmentRankingBinding

class RankingFragment : Fragment(R.layout.fragment_ranking) {

    // 참조 관리
    private var _binding : FragmentRankingBinding? = null
    private val binding get() = _binding!!

    // rankingAdapter 선언언
   private lateinit var rankingAdapter: RankingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰바인딩
        _binding = FragmentRankingBinding.bind(view)

        // 툴바 텍스트 변경
        val toolbar : androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.title = "점수 랭킹"

        binding.apply {
            rankingAdapter = RankingAdapter()
            rankingRecyclerView.adapter = rankingAdapter
        }
    }


    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
