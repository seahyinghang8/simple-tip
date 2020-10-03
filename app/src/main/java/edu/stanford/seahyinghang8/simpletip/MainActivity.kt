package edu.stanford.seahyinghang8.simpletip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

enum class ContainerSelected {
    BASE, TIP
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        baseContainer.setOnClickListener {
            highlightContainer(ContainerSelected.BASE)
        }

        tipContainer.setOnClickListener {
            highlightContainer(ContainerSelected.TIP)
        }
    }

    private fun highlightContainer(selected: ContainerSelected) {
        baseHighlight.visibility = View.GONE
        tipHighlight.visibility = View.GONE

        when (selected) {
            ContainerSelected.BASE -> baseHighlight.visibility = View.VISIBLE
            ContainerSelected.TIP -> tipHighlight.visibility = View.VISIBLE
        }
    }
}