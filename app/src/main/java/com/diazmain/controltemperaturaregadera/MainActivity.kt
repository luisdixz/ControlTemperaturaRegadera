package com.diazmain.controltemperaturaregadera

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import com.google.firebase.database.*
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ValueEventListener {

    val ref: FirebaseDatabase = FirebaseDatabase.getInstance()
    val root = ref.getReference("root")

    private lateinit var sensorRef: DatabaseReference
    private lateinit var estadoRef: DatabaseReference
    private lateinit var litrosRef: DatabaseReference
    private lateinit var idealRef: DatabaseReference

    private var tempIdeal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        estadoRef = root.child("estado")
        litrosRef = root.child("litros")
        sensorRef = root.child("sensor")
        idealRef = root.child("ideal")

        estadoRef.addValueEventListener(this)
        idealRef.addValueEventListener(this)
        litrosRef.addValueEventListener(this)
        sensorRef.addValueEventListener(this)

        btnEstablecer.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                idealRef.setValue(tempIdeal)
            }
        })

        swSistema.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    llContent.alpha = 1f
                    estadoRef.setValue(true)
                } else {
                    llContent.alpha = .45f
                    estadoRef.setValue(false)
                }
            }
        })

        snpIdeal.setListener(object : ScrollableNumberPickerListener{
            override fun onNumberPicked(value: Int) {
                tempIdeal = value
            }
        })
    }


    override fun onCancelled(p0: DatabaseError) {

    }

    override fun onDataChange(p0: DataSnapshot) {
        when (p0.key) {
            "sensor" -> {
                tvActual.text = p0.value.toString()+ " °C"
            }
            "ideal" -> {
                snpIdeal.value = Integer.parseInt(p0.value.toString())
                tvIdeal.text = p0.value.toString() + " °C"
            }
            "litros" -> {
                tvLitros.text = p0.value.toString()+" lt/m"
            }
            "estado" -> {
                //tvEstado.text = p0.value.toString() + " °C"
                /*if (!(p0.value as Boolean)) {
                    swSistema.isChecked = false
                    llContent.alpha = .45f
                } else {
                    swSistema.isChecked = true
                    llContent.alpha = 1f
                }*/
            }
        }
    }

    /*override fun onPause() {
        super.onPause()
        estadoRef.setValue(false)
        swSistema.isChecked = false
        llContent.alpha = .45f
    }*/
}
