package com.hanyeop.happysharing.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.adapter.ListAdapter
import com.hanyeop.happysharing.databinding.FragmentListBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.R.menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView


class ListFragment : Fragment(R.layout.fragment_list), ListAdapter.OnItemClickListener {

    // 참조 관리
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    // ListAdapter 선언
    private lateinit var listAdapter: ListAdapter

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰바인딩
        _binding = FragmentListBinding.bind(view)

        // 툴바 텍스트 변경
        val toolbar : androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.title = "나눔 목록"

        // 프래그먼트 툴바 버튼 생성
        setHasOptionsMenu(true)

        // 프로필 불러오기 (없으면 생성)
        val uId = FirebaseAuth.getInstance().currentUser?.uid
        firebaseViewModel.profileLoad(uId!!)

        binding.apply {
            // 리사이클러뷰 어댑터 연결
            listAdapter = ListAdapter(this@ListFragment)
            recyclerView.adapter = listAdapter

            // 스와이프하여 새로고침
            pullToRefresh.setOnRefreshListener {
                listAdapter.notifyDataSetChanged()
                pullToRefresh.isRefreshing = false
            }
        }
    }

    // 검색 버튼 생성
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)

        // 아이템 연결
        val searchItem = menu.findItem(R.id.searchButton)
        val searchView = searchItem.actionView as SearchView?

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            // 제출 버튼 눌렀을 때
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    val action = ListFragmentDirections.actionListFragmentToSearchActivity(query)
                    findNavController().navigate(action)
                    searchView.clearFocus() // 포커스 없애기 (커서 없애기)
                }
                return true
            }

            // 검색어 값 변경시
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    // 아이템 클릭 시 디테일 뷰로
    override fun onItemClick(itemDTO: ItemDTO,userDTO: UserDTO) {
        val action = ListFragmentDirections.actionListFragmentToDetailActivity(itemDTO,userDTO)
        findNavController().navigate(action)
    }

    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 본인 업로드, 삭제 시 목록 갱신 위함
    override fun onResume() {
        super.onResume()
        listAdapter.notifyDataSetChanged()
    }
}
