package com.sammengistu.urbandictionaryapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sammengistu.urbandictionaryapp.DefinitionFactory
import com.sammengistu.urbandictionaryapp.DefinitionsRepository
import com.sammengistu.urbandictionaryapp.R
import com.sammengistu.urbandictionaryapp.adapters.DictionaryAdapter
import com.sammengistu.urbandictionaryapp.db.DefinitionDao
import com.sammengistu.urbandictionaryapp.db.UrbanDictionaryDb
import com.sammengistu.urbandictionaryapp.models.Definition
import com.sammengistu.urbandictionaryapp.network.RetrofitClientInstance
import com.sammengistu.urbandictionaryapp.network.UrbanDictionaryService
import com.sammengistu.urbandictionaryapp.viewmodels.DefinitionViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: DictionaryAdapter
    private var lastLookedUpWord: String? = null
    private var definitions: List<Definition> = ArrayList()
    private var sortByThumbsUp = true

    private val viewModel: DefinitionViewModel by viewModels(
        factoryProducer = {
            val service = RetrofitClientInstance.retrofit.create(UrbanDictionaryService::class.java)
            val dao: DefinitionDao = UrbanDictionaryDb.getInstance(context!!).definitionDao()
            DefinitionFactory(DefinitionsRepository.getInstance(service, dao))
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editText = input_editText
        progressBar = progress_bar
        val searchButton = search_button

        recyclerView = recycler_view
        setUpRecyclerView()
        addObserver()
        searchButton.setOnClickListener {
            onSearchClicked()
        }
        createSpinner()
        // Build view from last state
        updateViewFromSavedState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save last typed text and last looked up word
        outState.putString(EXTRA_WORD, lastLookedUpWord)
        outState.putString(EXTRA_LAST_TEXT, editText.text.toString())
    }

    private fun onSearchClicked() {
        val word = editText.text.toString()
        if (word.isNotBlank()) {
            lastLookedUpWord = word
            lookUpWord(word)
        } else {
            Toast.makeText(activity, "Please enter a word", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createSpinner() {
        val spinner: Spinner = spinner
        ArrayAdapter.createFromResource(
            context!!,
            R.array.sort_list_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = getSpinnerClickListener()
    }

    private fun getSpinnerClickListener(): AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                parent?.getItemAtPosition(0)
                sortByThumbsUp = true
                setDataOnAdapter()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                sortByThumbsUp = position == 0
                setDataOnAdapter()
            }
        }

    private fun updateViewFromSavedState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val lastWord = savedInstanceState.getString(EXTRA_WORD)
            val lastText = savedInstanceState.getString(EXTRA_LAST_TEXT)

            if (!lastWord.isNullOrEmpty()) {
                lastLookedUpWord = lastWord
                lookUpWord(lastWord)
            }

            if (!lastText.isNullOrEmpty()) {
                editText.setText(lastText)
            }
        }
    }

    private fun lookUpWord(word: String) {
        progressBar.visibility = View.VISIBLE
        viewModel.setTerm(word)
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context).also {
            adapter = DictionaryAdapter(context!!)
            recyclerView.adapter = adapter
        }
    }

    private fun addObserver() {
        viewModel.definitionModels.removeObservers(viewLifecycleOwner)
        viewModel.definitionModels.observe(viewLifecycleOwner) { definitions ->
            progressBar.visibility = View.GONE
            if (definitions.isEmpty()) {
                Toast.makeText(context, "Error/No definitions", Toast.LENGTH_SHORT).show()
            } else {
                this.definitions = definitions
                setDataOnAdapter()
            }
        }
    }

    private fun setDataOnAdapter() {
        if (sortByThumbsUp) {
            Collections.sort(this.definitions, Definition.getThumbsUpComparator())
        } else {
            Collections.sort(this.definitions, Definition.getThumbsDownComparator())
        }
        adapter.setData(definitions)
    }

    companion object {
        val TAG = MainFragment::class.java.simpleName
        const val EXTRA_WORD = "extra_word"
        const val EXTRA_LAST_TEXT = "extra_last_text"
    }
}