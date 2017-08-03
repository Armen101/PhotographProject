package com.example.student.userphotograph.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.adapter.AboutRecyclerAdapter;
import com.example.student.userphotograph.models.AboutModel;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {

    private List<AboutModel> aboutModelList;
    private TextView tvInfo;

    public AboutFragment() {
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        aboutModelList = new ArrayList<>();
        findViewById(rootView);
        recyclerView(rootView);
        prepareAboutData();

        tvInfo.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://vtc.am/am/'> @2017 VTC Android team   " +
                "http://vtc.am </a>";
        tvInfo.setText(Html.fromHtml(text));
        return rootView;
    }

    private void recyclerView(View rootView) {
        RecyclerView myRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_about);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myRecycler.setLayoutManager(mLayoutManager);
        myRecycler.setHasFixedSize(true);
        myRecycler.setAdapter(new AboutRecyclerAdapter(aboutModelList, getActivity()));
    }

    private void findViewById(View rootView) {
        ImageView imgLogo = (ImageView) rootView.findViewById(R.id.img_logo);
        TextView tvOwner = (TextView) rootView.findViewById(R.id.tv_owner);
        TextView tvOwnerName = (TextView) rootView.findViewById(R.id.tv_owner_name_surname);
        TextView tvMembers = (TextView) rootView.findViewById(R.id.tv_members);
        TextView tvAboutApp = (TextView) rootView.findViewById(R.id.about_app);
        tvInfo = (TextView) rootView.findViewById(R.id.vtc_and_team);
    }

    private void prepareAboutData() {
        AboutModel modelSmbat = new AboutModel("https://scontent-frx5-1.xx.fbcdn.net/v/t1.0-9/18034353_1439636092759332_5653660638961461223_n.jpg?oh=e1241a912a1556e1b53c02975a4c1c8b&oe=59F36296", "Smbat Sargsyan");
        aboutModelList.add(modelSmbat);
        AboutModel modelNarek = new AboutModel("https://fb-s-c-a.akamaihd.net/h-ak-fbx/v/t34.0-12/20496535_871467776341382_1118183471_n.jpg?oh=9af30f8081c4c74a98b566a559467406&oe=5980304D&__gda__=1501508098_4792c2ef114ec6b4f0adce3d23ce70de", "Narek Jaghatspanyan");
        aboutModelList.add(modelNarek);
        AboutModel modelNelly = new AboutModel("https://fb-s-d-a.akamaihd.net/h-ak-fbx/v/t31.0-8/16251924_1350564415017013_4706790399921524550_o.jpg?oh=5c2a2a144530b47822894f7e000f3374&oe=59EFA094&__gda__=1508864586_5392a809ee275d11781fcde08b183a82", "Nelly Galstyan");
        aboutModelList.add(modelNelly);
        AboutModel modelIrina = new AboutModel("https://scontent-frx5-1.xx.fbcdn.net/v/t31.0-8/15392768_540083716186585_370364526249021249_o.jpg?oh=9b59fa976423156460dd2c877df1ad35&oe=59EE6B6F", "Irina Kazazyan");
        aboutModelList.add(modelIrina);
        AboutModel modelBabken = new AboutModel("https://scontent-frx5-1.xx.fbcdn.net/v/t1.0-9/15253564_344174009274877_6314456752230016379_n.jpg?oh=58b47fc5f284014871cd5b06e37c97e5&oe=5A37A80C", "Babken Mxitaryan");
        aboutModelList.add(modelBabken);
        AboutModel modelTaron = new AboutModel("https://fb-s-a-a.akamaihd.net/h-ak-fbx/v/t31.0-8/16422323_1187968791321854_3393052890860661066_o.jpg?oh=bd12cd2cd43ac4d97ca2642fe8817b0a&oe=59F7BEA2&__gda__=1513491324_8051ca0891ded42203b39f89e6fb9831", "Taron Mkrtchyan");
        aboutModelList.add(modelTaron);
        AboutModel modelSeryan = new AboutModel("https://fb-s-d-a.akamaihd.net/h-ak-fbx/v/t1.0-9/300361_150382331717695_952750827_n.jpg?oh=cd8635597b859a277fe7f7473a276ab5&oe=59EC2675&__gda__=1509655836_e347de9568fd5f90a815a5ecf35b84f7", "Seryan Alavaerdyan");
        aboutModelList.add(modelSeryan);
        AboutModel modelArmen = new AboutModel("https://fb-s-a-a.akamaihd.net/h-ak-fbx/v/t1.0-9/18156993_1842107492718404_1709764772615232190_n.jpg?oh=1c6338357ff192a489f2e757fdbe2928&oe=59F3F0A9&__gda__=1509541954_d844f6b2224363774bb6068b66108d65", "Armen Gevorgyan");
        aboutModelList.add(modelArmen);
        AboutModel modelArman = new AboutModel("https://fb-s-b-a.akamaihd.net/h-ak-fbx/v/t1.0-9/18275074_1168308429945233_1387811948415189625_n.jpg?oh=d1e6220cada0925ce098d29981b630e1&oe=5A044B24&__gda__=1513228495_bb3fba2753cd6f4c1ce4aaee6ceefd04", "Arman Vardanyan");
        aboutModelList.add(modelArman);
        AboutModel modelAnna = new AboutModel("", "Anna Hovhannisyan");
        aboutModelList.add(modelAnna);
    }
}