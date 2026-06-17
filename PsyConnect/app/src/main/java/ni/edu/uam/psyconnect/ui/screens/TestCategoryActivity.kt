package ni.edu.uam.psyconnect.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uam.psyconnect.R
import ni.edu.uam.psyconnect.data.model.TestCategory
import ni.edu.uam.psyconnect.ui.adapter.TestCategoryAdapter

class TestCategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_test_category
        )

        val recyclerCategories =
            findViewById<RecyclerView>(
                R.id.recyclerCategories
            )

        recyclerCategories.layoutManager =
            GridLayoutManager(
                this,
                2
            )

        recyclerCategories.adapter =
            TestCategoryAdapter(

                TestCategory.entries.toList(),

                this
            )
    }
}