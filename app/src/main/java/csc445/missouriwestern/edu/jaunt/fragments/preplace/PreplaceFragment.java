package csc445.missouriwestern.edu.jaunt.fragments.preplace;


import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import csc445.missouriwestern.edu.jaunt.Globals;
import csc445.missouriwestern.edu.jaunt.R;
import csc445.missouriwestern.edu.jaunt.extensions.adapters.CustomSectionedRecyclerViewAdapter;
import csc445.missouriwestern.edu.jaunt.model.PlaceHistoryRecord;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import io.paperdb.Paper;

import static java.util.stream.Collectors.toList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreplaceFragment extends Fragment {

    List<PlaceHistoryRecord> thisWeekRecords = new ArrayList<>();
    List<PlaceHistoryRecord> lastWeekRecords = new ArrayList<>();
    List<PlaceHistoryRecord> otherRecords    = new ArrayList<>();
    ArrayList<Place> section1Addresses = new ArrayList<>();
    ArrayList<Place> section2Addresses = new ArrayList<>();
    OnDataPass dataPasser;
    final CustomSectionedRecyclerViewAdapter sectionAdapter = new CustomSectionedRecyclerViewAdapter();
    CustomSectionedRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener;

    public PreplaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_preplace, container, false);

        // Create an instance of SectionedRecyclerViewAdapter (changed to final global)
        // Borrowed from https://github.com/luizgrp/SectionedRecyclerViewAdapter
        listener = new CustomSectionedRecyclerViewAdapter.RecyclerViewItemOnClickedListener() {
            @Override
            public void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder) {
                Address selectedAddress = ((PlaceItemViewHolder)viewHolder).address;
                dataPasser.onDataPass(selectedAddress);
            }
        };

        sectionAdapter.setListener(listener);

        getSearchHistory();

        if(thisWeekRecords.size() > 0) {
            sectionAdapter.addSection(new PlaceSection("This week", thisWeekRecords));
        }
        if(lastWeekRecords.size() > 0) {
            sectionAdapter.addSection(new PlaceSection("Last week", lastWeekRecords));
        }
        if(otherRecords.size() > 0) {
            sectionAdapter.addSection(new PlaceSection("Previous Searches", otherRecords));
        }

        // Set up your RecyclerView with the SectionedRecyclerViewAdapter
        RecyclerView recyclerView = fragmentView.findViewById(R.id.place_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dataPasser = (OnDataPass) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        getSearchHistory();
    }

    class PlaceSection extends StatelessSection {

        String title;
        List<PlaceHistoryRecord> itemList = new ArrayList<>();

        public PlaceSection(String sectionTitle, List<PlaceHistoryRecord> records) {
            // call constructor with layout resources for this Section header and items
            super(new SectionParameters.Builder(R.layout.place_section_item)
                    .headerResourceId(R.layout.place_section_header)
                    .build());
            itemList = records;
            title = sectionTitle;
        }

        @Override
        public int getContentItemsTotal() {
            return itemList.size(); // number of items of this section
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            // return a custom instance of ViewHolder for the items of this section
            return new PlaceItemViewHolder(getActivity(), view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            Address address = itemList.get(position).getAddress();
            // bind your view here
            // code moved to the holder class itself with a listener for click event
            ((PlaceItemViewHolder) holder).bindView(address, listener);
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new PlaceSectionHeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            PlaceSectionHeaderViewHolder headerHolder = (PlaceSectionHeaderViewHolder) holder;
            headerHolder.sectionTitleTextView.setText(title);
        }
    }

    public void getSearchHistory(){

        List<PlaceHistoryRecord> allRecords;
        allRecords = Paper.book().read(Globals.PLACE_HISTORY_KEY, new ArrayList<PlaceHistoryRecord>());
        // Add your Sections
        thisWeekRecords = getThisWeekRecords(allRecords);
        lastWeekRecords = getLastWeekRecords(allRecords);
        otherRecords = getRecordsBeforeLastWeek(allRecords);
    }

    public List<PlaceHistoryRecord> getThisWeekRecords(List<PlaceHistoryRecord> allRecords){
        Stream<PlaceHistoryRecord> filteredStream = allRecords.stream().filter(element -> isSearchedThisWeek(element));
        List<PlaceHistoryRecord> filteredList = filteredStream.collect(toList());
        return filteredList;
    }

    public List<PlaceHistoryRecord> getLastWeekRecords(List<PlaceHistoryRecord> allRecords){
        Stream<PlaceHistoryRecord> filteredStream = allRecords.stream().filter(element -> isSearchedLastWeek(element));
        List<PlaceHistoryRecord> filteredList = filteredStream.collect(toList());
        return filteredList;
    }

    public List<PlaceHistoryRecord> getRecordsBeforeLastWeek(List<PlaceHistoryRecord> allRecords){
        Stream<PlaceHistoryRecord> filteredStream = allRecords.stream().filter(element -> isSearchedBeforeLastWeek(element));
        List<PlaceHistoryRecord> filteredList = filteredStream.collect(toList());
        return filteredList;
    }

    public boolean isSearchedThisWeek(PlaceHistoryRecord record){
        LocalDate recordDate = record.getLastSearchTime().withZone(DateTimeZone.getDefault()).toLocalDate();
        LocalDate currentDate = new LocalDate(DateTimeZone.getDefault());

        LocalDate lastWeek = currentDate.minusWeeks(1);
        LocalDate endLastWeek   = lastWeek.dayOfWeek().withMaximumValue().minusDays(1);

        if(recordDate.compareTo(endLastWeek) > 0){
            return true;
        }
        return false;
    }

    public boolean isSearchedLastWeek(PlaceHistoryRecord record){

        LocalDate recordDate = record.getLastSearchTime().withZone(DateTimeZone.getDefault()).toLocalDate();
        LocalDate currentDate = new LocalDate(DateTimeZone.getDefault());

        LocalDate lastWeek = currentDate.minusWeeks(1);
        LocalDate startLastWeek = lastWeek.dayOfWeek().withMinimumValue().minusDays(1);
        LocalDate endLastWeek   = lastWeek.dayOfWeek().withMaximumValue().minusDays(1);

        if(recordDate.compareTo(startLastWeek) >= 0 && recordDate.compareTo(endLastWeek) <= 0){
            return true;
        }
        return false;
    }

    public boolean isSearchedBeforeLastWeek(PlaceHistoryRecord record){
        LocalDate recordDate = record.getLastSearchTime().withZone(DateTimeZone.getDefault()).toLocalDate();
        LocalDate currentDate = new LocalDate(DateTimeZone.getDefault());

        LocalDate lastWeek = currentDate.minusWeeks(1);
        LocalDate startLastWeek = lastWeek.dayOfWeek().withMinimumValue().minusDays(1);
        if(recordDate.compareTo(startLastWeek) < 0){
            return true;
        }
        return false;
    }

    public interface OnDataPass {
        public void onDataPass(Address data);
    }
}
