package com.example.lab7

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi

import androidx.compose.material.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab7.ui.theme.FirebaseprojectTheme
import com.example.lab7.ui.theme.greenColor
import com.example.lab7.ui.theme.Lab7Theme

import com.google.firebase.firestore.FirebaseFirestore

class CourseDetailsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab7Theme {

                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {

                            Scaffold(
                                // in scaffold we are specifying the top bar.
                                topBar = {
                                    // inside top bar we are
                                    // specifying background color.
                                    TopAppBar(
                                        // along with that we are specifying
                                        // title for our top bar.
                                        title = {
                                            // in the top bar we are
                                            // specifying tile as a text
                                            Text(
                                                // on below line we are specifying
                                                // text to display in top app bar
                                                text = "GFG",
                                                // on below line we are specifying
                                                // modifier to fill max width
                                                modifier = Modifier.fillMaxWidth(),
                                                // on below line we are specifying
                                                // text alignment
                                                textAlign = TextAlign.Center,
                                                // on below line we are specifying
                                                // color for our text.
                                                color = Color.White
                                            )
                                        })
                                }) { innerPadding ->
                                Text(
                                    modifier = Modifier.padding(innerPadding),
                                    text = "Cap nhat du lieu."
                                )


                                val courseList = mutableStateListOf<Course?>()

                                // and database reference.
                                val db: FirebaseFirestore =
                                    FirebaseFirestore.getInstance()

                                // on below line getting data from our database
                                db.collection("Courses").get()
                                    .addOnSuccessListener { queryDocumentSnapshots ->
                                        // after getting the data we are
                                        // calling on success method
                                        // and inside this method we are
                                        // checking if the received query
                                        // snapshot is empty or not.
                                        if (!queryDocumentSnapshots.isEmpty) {
                                            // if the snapshot is not empty we are
                                            // hiding our progress bar and adding
                                            // our data in a list.
                                            // loadingPB.setVisibility(View.GONE)
                                            val list =
                                                queryDocumentSnapshots.documents
                                            for (d in list) {

                                                // to our object class.
                                                val c: Course? =
                                                    d.toObject(Course::class.java)
                                                c?.courseId = d.id
                                                Log.e("TAG", "Course id is : " +
                                                        c!!.courseId)
                                                // and we will pass this object
                                                class
                                                // inside our arraylist which we
                                                have
                                                // created for list view.
                                                courseList.add(c)

                                            }
                                        } else {
                                            // if the snapshot is empty we are
                                            // displaying a toast message.
                                            Toast.makeText(
                                                this@CourseDetailsActivity,
                                                "No data found in Database",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    // if we don't get any data
                                    // or any error we are displaying
                                    // a toast message that we donot get any data
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this@CourseDetailsActivity,
                                            "Fail to get the data.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                // on below line we are calling method to display
                                FirebaseUI(LocalContext.current, courseList)

                                // ket thuc

                            }
                        }
            }
        }
    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun FirebaseUI(context: Context, courseList:
    SnapshotStateList<Course?>) {

        // on below line creating a column
        // to display our retrieved list.
        Column(
            // adding modifier for our column
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White),
            // on below line adding vertical and
            // horizontal alignment for column.
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // on below line we are calling lazy column
            // for displaying listview.
            LazyColumn {
                // on below line we are setting data
                // for each item of our listview.
                itemsIndexed(courseList) { index, item ->
                    // on below line we are creating
                    // a card for our list view item.
                    Card(
                        onClick = {
                            val i = Intent(context,
                                UpdateCourse::class.java)
                            i.putExtra("courseName", item?.courseName)
                            i.putExtra("courseDuration",
                                item?.courseDuration)
                            i.putExtra("courseDescription",
                                item?.courseDescription)
                            i.putExtra("courseID", item?.courseId)
                            // inside on click we are opening
                            // new activity to update course details.
                            context.startActivity(i)

                        },
                        // on below line we are adding
                        // padding from our all sides.
                        modifier = Modifier.padding(8.dp),

                        // on below line we are adding
                        // elevation for the card.
                        elevation = 6.dp
                    ) {
                        // on below line we are creating
                        // a row for our list view item.
                        Column(
                            // for our row we are adding modifier
                            // to set padding from all sides.
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {                            val greenColor = Color(0xFF4CAF50) // Màu xanh lá cây


                            Spacer(modifier = Modifier.width(5.dp))
                            // on below line we are displaying course name.
                            courseList[index]?.courseName?.let {
                                Text(
                                    // inside the text on below line we are
                                    // setting text as the language name
                                    // from our modal class.
                                    text = it,

                                    // on below line we are adding padding
                                    // for our text from all sides.
                                    modifier = Modifier.padding(4.dp),

                                            color = greenColor,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 20.sp, fontWeight =
                                        FontWeight.Bold
                                    )
                                )
                            }
                            // adding spacer on below line.
                            Spacer(modifier = Modifier.height(5.dp))

                            courseList[index]?.courseDuration?.let {
                                Text(
                                    // inside the text on below line we are
                                    // setting text as the language name
                                    // from our modal class.
                                    text = it,

                                    // on below line we are adding padding
                                    // for our text from all sides.
                                    modifier = Modifier.padding(4.dp),
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 15.sp
                                    )
                                )
                            }
                            // adding spacer on below line.
                            Spacer(modifier = Modifier.width(5.dp))

                            // on below line displaying text
                            // for course description
                            courseList[index]?.courseDescription?.let {
                                Text(
                                    // inside the text on below line we are
                                    // setting text as the language name
                                    // from our modal class.
                                    text = it,

                                    // on below line we are adding padding
                                    // for our text from all sides.
                                    modifier = Modifier.padding(4.dp),

                                    // on below line we are
                                    // adding color for our text
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontSize = 15.sp)
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}