package com.example.horse_attila

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.horse_attila.databinding.ActivityMainBinding
import org.example.BFS
import org.example.DFS
import org.example.IDDFS
import org.example.DoubleBFS
import org.example.GameField
import org.example.State
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import org.example.ChebyshevMetricBFS
import org.example.HeuristicBFS
import org.example.MetricsHorsBFS

class MainActivity : AppCompatActivity() {
    private  var BFS: BFS = BFS()
    private  var DFS: DFS = DFS()
    private  var DoubleBFS: DoubleBFS = DoubleBFS()
    private var Manhattan: HeuristicBFS = HeuristicBFS()
    private var MetricsHorsBFS : MetricsHorsBFS = MetricsHorsBFS()
    private  var ChebyshevMetricBFS: ChebyshevMetricBFS = ChebyshevMetricBFS()
    private  var IDDFS: IDDFS = IDDFS()
    private  var dfs_bool: Boolean = false
    private  var bfs_bool: Boolean = false
    private  var d_bfs_bool: Boolean = false
    private var manhattan_bool: Boolean = false
    private var metric_horse_bool: Boolean = false
    private var metric_chebushev_bool: Boolean = false
    private  var hash_p_size: Int = 0
    private  var iddfs_bool: Boolean = false
    private lateinit var binding: ActivityMainBinding
    private  var stateList: List<State> = listOf()
    private var currentToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val squaresView = findViewById<SquaresView>(R.id.squaresView)
        squaresView.changeSize(8)
        var desk_size = 8
        initializeSpinner(squaresView)
        with(binding) {

            checkMethod.setOnClickListener {
                squaresView.currentIndex =0
                binding.tvChessHod.text = "0"
                squaresView.clearGreenFields()
                if (bfs_bool) {
                    performBFS(squaresView)
                }
                else if (dfs_bool) {
                    performDFS(squaresView)
                }
                else if (d_bfs_bool) {
                    performDoubleBFS(squaresView)
                }
                else if (iddfs_bool) {
                    performIDDFS(squaresView)
                }
                else if (manhattan_bool) {
                    performManhattan(squaresView)
                }
                else if (metric_horse_bool) {
                    performMetricHorseBFS(squaresView)
                }
                else if (metric_chebushev_bool) {
                    performChebushevBFS(squaresView)
                }
            }
            buttonForward.setOnClickListener {
                if(stateList.size != 0){
                squaresView.moveForward()
                tvChessHod.text = squaresView.currentIndex.toString()
            }}
            buttonBack.setOnClickListener {
                squaresView.moveBackward()
                tvChessHod.text = squaresView.currentIndex.toString()
            }

            tvSizeDesk.text = desk_size.toString()
            buttonPlusDesk.setOnClickListener {
                desk_size++
                squaresView.changeSize(desk_size )
                tvSizeDesk.text = desk_size.toString()
            }
            buttonMinusDesk.setOnClickListener {
                if(desk_size == 3) return@setOnClickListener
                desk_size--
                squaresView.changeSize(desk_size )
                tvSizeDesk.text = desk_size.toString()
            }

        }

    }
    private fun initializeSpinner(squaresView: SquaresView) {
        val items = listOf("Choose Method", "BFS", "DFS","Double BFS", "IDDFS","Manhattan","MetricsHorsBFS","ChebushevBFS")
        val adapter = ArrayAdapter(this, R.layout.tv_dropdown_spinner, items)
        adapter.setDropDownViewResource(R.layout.tv_white)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                when (selectedItem) {
                    "BFS" ->{
                        bfs_bool = true
                        dfs_bool = false
                        d_bfs_bool = false
                        iddfs_bool = false
                        manhattan_bool = false
                        metric_horse_bool = false
                        metric_chebushev_bool = false
                    }
                    "DFS" -> {
                        dfs_bool = true
                        bfs_bool = false
                        d_bfs_bool = false
                        iddfs_bool = false
                        manhattan_bool = false
                        metric_horse_bool = false
                        metric_chebushev_bool = false
                    }
                    "Double BFS" ->{
                        d_bfs_bool = true
                        bfs_bool = false
                        dfs_bool = false
                        iddfs_bool = false
                        manhattan_bool = false
                        metric_horse_bool = false
                        metric_chebushev_bool = false
                    }
                    "IDDFS" ->{
                        iddfs_bool = true
                        d_bfs_bool = false
                        bfs_bool = false
                        dfs_bool = false
                        manhattan_bool = false
                        metric_horse_bool = false
                        metric_chebushev_bool = false
                    }
                    "Manhattan" ->{
                        iddfs_bool = false
                        d_bfs_bool = false
                        bfs_bool = false
                        dfs_bool = false
                        manhattan_bool = true
                        metric_horse_bool = false
                        metric_chebushev_bool = false
                    }
                    "MetricsHorsBFS" ->{
                        iddfs_bool = false
                        d_bfs_bool = false
                        bfs_bool = false
                        dfs_bool = false
                        manhattan_bool = false
                        metric_horse_bool = true
                        metric_chebushev_bool = false
                    }
                    "ChebushevBFS" ->{
                        iddfs_bool = false
                        d_bfs_bool = false
                        bfs_bool = false
                        dfs_bool = false
                        manhattan_bool = false
                        metric_horse_bool = false
                        metric_chebushev_bool = true
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }


    private fun performBFS(squaresView: SquaresView) {
        binding.tvChessHod.text = "0"
        stateList = BFS.search(squaresView.StateHorse,  squaresView.StateKing,  squaresView.GameField)
        val layout = layoutInflater.inflate(R.layout.toast_answer, null)
        val textView: TextView = layout.findViewById(R.id.toast_text)
        val layoutContainer: LinearLayout = layout.findViewById(R.id.toast_message)
        if(stateList.size == 0 || stateList[0]!= stateList[stateList.size-1]) {
            layoutContainer.setBackgroundResource(R.drawable.shape_red_toast)
            textView.text = "Решение не найдено!"
        }
        else{
            textView.text = "Решение найдено!"
        }
        currentToast?.cancel()


        currentToast = Toast(applicationContext).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
        hash_p_size =BFS.maxQueueSize + BFS.mAxHashSize
        binding.tvIter.text = "Iter: "+BFS.iterationCount.toString()
        binding.tvListsize.text = "MaxListSize: "+BFS.maxQueueSize.toString()
        binding.tvMaxmemory.text = "MaxHash: "+BFS.mAxHashSize.toString()
        binding.tvQueueize.text = "Hash + List: "+ hash_p_size.toString()
        squaresView.stateList = stateList
    }

    private fun performDoubleBFS(squaresView: SquaresView) {
        binding.tvChessHod.text = "0"
        stateList = DoubleBFS.search(squaresView.StateHorse,  squaresView.StateKing,  squaresView.GameField)
        val layout = layoutInflater.inflate(R.layout.toast_answer, null)
        val textView: TextView = layout.findViewById(R.id.toast_text)
        val layoutContainer: LinearLayout = layout.findViewById(R.id.toast_message)
        if(stateList.size == 0 || stateList[0]!= stateList[stateList.size-1]) {
            layoutContainer.setBackgroundResource(R.drawable.shape_red_toast)
            textView.text = "Решение не найдено!"
        }
        else{
            textView.text = "Решение найдено!"
        }
        currentToast?.cancel()


        currentToast = Toast(applicationContext).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }

        hash_p_size =DoubleBFS.maxQueue + DoubleBFS.maxCloseList
        binding.tvIter.text = "Iter: "+DoubleBFS.iterationCount.toString()
        binding.tvListsize.text = "MaxListSize: "+DoubleBFS.maxQueue.toString()
        binding.tvMaxmemory.text = "MaxHash: "+DoubleBFS.maxCloseList.toString()
        binding.tvQueueize.text = "Hash + List: "+ hash_p_size.toString()
        squaresView.stateList = stateList
    }
    private fun performDFS(squaresView: SquaresView) {
        binding.tvChessHod.text = "0"
        stateList = DFS.search(squaresView.StateHorse,  squaresView.StateKing,  squaresView.GameField)

        val layout = layoutInflater.inflate(R.layout.toast_answer, null)
        val textView: TextView = layout.findViewById(R.id.toast_text)
        val layoutContainer: LinearLayout = layout.findViewById(R.id.toast_message)
        if(stateList.size == 0 || stateList[0]!= stateList[stateList.size-1]) {
            layoutContainer.setBackgroundResource(R.drawable.shape_red_toast)
            textView.text = "Решение не найдено!"
        }
        else{
            textView.text = "Решение найдено!"
        }

        currentToast?.cancel()


        currentToast = Toast(applicationContext).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
        hash_p_size =DFS.maxHashSize + DFS.maxStackSize
        binding.tvIter.text = "Iter: "+DFS.iterationCount.toString()
        binding.tvListsize.text = "MaxListSize: "+DFS.maxStackSize.toString()
        binding.tvMaxmemory.text = "MaxHash: "+DFS.maxHashSize.toString()
        binding.tvQueueize.text = "Hash + List: "+ hash_p_size.toString()
        squaresView.stateList = stateList
    }
    private fun performIDDFS(squaresView: SquaresView) {
        binding.tvChessHod.text = "0"
        stateList = IDDFS.search(squaresView.StateHorse,  squaresView.StateKing,  squaresView.GameField)

        val layout = layoutInflater.inflate(R.layout.toast_answer, null)
        val textView: TextView = layout.findViewById(R.id.toast_text)
        val layoutContainer: LinearLayout = layout.findViewById(R.id.toast_message)
        if(stateList.size == 0 || stateList[0]!= stateList[stateList.size-1]) {
            layoutContainer.setBackgroundResource(R.drawable.shape_red_toast)
            textView.text = "Решение не найдено!"
        }
        else{
            textView.text = "Решение найдено!"
        }

        currentToast?.cancel()


        currentToast = Toast(applicationContext).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
        hash_p_size =IDDFS.maxStackSize + IDDFS.maxHashSize
        binding.tvIter.text = "Iter: "+IDDFS.iterationCount.toString()
        binding.tvListsize.text = "MaxListSize: "+IDDFS.maxStackSize.toString()
        binding.tvMaxmemory.text = "MaxHash: "+IDDFS.maxHashSize.toString()
        binding.tvQueueize.text = "Hash + List: "+ hash_p_size.toString()
        squaresView.stateList = stateList
    }
    private fun performManhattan(squaresView: SquaresView) {
        binding.tvChessHod.text = "0"
        stateList = Manhattan.search(squaresView.StateHorse,  squaresView.StateKing,  squaresView.GameField)

        val layout = layoutInflater.inflate(R.layout.toast_answer, null)
        val textView: TextView = layout.findViewById(R.id.toast_text)
        val layoutContainer: LinearLayout = layout.findViewById(R.id.toast_message)
        if(stateList.size == 0 || stateList[0]!= stateList[stateList.size-1]) {
            layoutContainer.setBackgroundResource(R.drawable.shape_red_toast)
            textView.text = "Решение не найдено!"
        }
        else{
            textView.text = "Решение найдено!"
        }

        currentToast?.cancel()


        currentToast = Toast(applicationContext).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
        hash_p_size =Manhattan.maxQueueSize + Manhattan.maxHashSize
        binding.tvIter.text = "Iter: "+Manhattan.iterationCount.toString()
        binding.tvListsize.text = "MaxListSize: "+Manhattan.maxQueueSize.toString()
        binding.tvMaxmemory.text = "MaxHash: "+Manhattan.maxHashSize.toString()
        binding.tvQueueize.text = "Hash + List: "+ hash_p_size.toString()
        squaresView.stateList = stateList
    }
    private fun performMetricHorseBFS(squaresView: SquaresView) {
        binding.tvChessHod.text = "0"
        stateList = MetricsHorsBFS.search(squaresView.StateHorse,  squaresView.StateKing,  squaresView.GameField)

        val layout = layoutInflater.inflate(R.layout.toast_answer, null)
        val textView: TextView = layout.findViewById(R.id.toast_text)
        val layoutContainer: LinearLayout = layout.findViewById(R.id.toast_message)
        if(stateList.size == 0 || stateList[0]!= stateList[stateList.size-1]) {
            layoutContainer.setBackgroundResource(R.drawable.shape_red_toast)
            textView.text = "Решение не найдено!"
        }
        else{
            textView.text = "Решение найдено!"
        }

        currentToast?.cancel()


        currentToast = Toast(applicationContext).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
        hash_p_size =MetricsHorsBFS.queueSize + MetricsHorsBFS.maxHashSize
        binding.tvIter.text = "Iter: "+MetricsHorsBFS.iterationCount.toString()
        binding.tvListsize.text = "MaxListSize: "+MetricsHorsBFS.queueSize.toString()
        binding.tvMaxmemory.text = "MaxHash: "+MetricsHorsBFS.maxHashSize.toString()
        binding.tvQueueize.text = "Hash + List: "+ hash_p_size.toString()
        squaresView.stateList = stateList
    }

    private fun performChebushevBFS(squaresView: SquaresView) {
        binding.tvChessHod.text = "0"
        stateList = ChebyshevMetricBFS.search(squaresView.StateHorse,  squaresView.StateKing,  squaresView.GameField)

        val layout = layoutInflater.inflate(R.layout.toast_answer, null)
        val textView: TextView = layout.findViewById(R.id.toast_text)
        val layoutContainer: LinearLayout = layout.findViewById(R.id.toast_message)
        if(stateList.size == 0 || stateList[0]!= stateList[stateList.size-1]) {
            layoutContainer.setBackgroundResource(R.drawable.shape_red_toast)
            textView.text = "Решение не найдено!"
        }
        else{
            textView.text = "Решение найдено!"
        }

        currentToast?.cancel()


        currentToast = Toast(applicationContext).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
        hash_p_size =ChebyshevMetricBFS.queueSize + ChebyshevMetricBFS.maxHashSize
        binding.tvIter.text = "Iter: "+ChebyshevMetricBFS.iterationCount.toString()
        binding.tvListsize.text = "MaxListSize: "+ChebyshevMetricBFS.queueSize.toString()
        binding.tvMaxmemory.text = "MaxHash: "+ChebyshevMetricBFS.maxHashSize.toString()
        binding.tvQueueize.text = "Hash + List: "+ hash_p_size.toString()
        squaresView.stateList = stateList
    }

}