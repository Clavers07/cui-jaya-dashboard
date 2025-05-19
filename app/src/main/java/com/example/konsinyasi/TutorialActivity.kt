package com.example.konsinyasi

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konsinyasi.databinding.ActivityTutorialBinding

class TutorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.kembali.setOnClickListener {
            finish()
        }

        binding.tutor1.setOnClickListener{
            if (binding.contentTutor1.visibility == View.GONE) {
                binding.contentTutor1.visibility = View.VISIBLE
            } else {
                binding.contentTutor1.visibility = View.GONE
            }
        }
        binding.tutor2.setOnClickListener{
            if (binding.contentTutor2.visibility == View.GONE) {
                binding.contentTutor2.visibility = View.VISIBLE
            } else {
                binding.contentTutor2.visibility = View.GONE
            }
        }
        binding.tutor3.setOnClickListener{
            if (binding.contentTutor3.visibility == View.GONE) {
                binding.contentTutor3.visibility = View.VISIBLE
            } else {
                binding.contentTutor3.visibility = View.GONE
            }
        }


    }
}