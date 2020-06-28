package com.aslanovaslan.instakloneapp.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.aslanovaslan.instakloneapp.R
import com.aslanovaslan.instakloneapp.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class ProfileEditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var imgUserProfilePhotos: CircleImageView
    private lateinit var progressBarEditFragment: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        imgUserProfilePhotos = view.findViewById(R.id.imgUserProfilePhotos)
        progressBarEditFragment = view.findViewById(R.id.progressBarEditFragment)

        setupProfilePhotos()
        view.imgCloseProfile.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

    private fun setupProfilePhotos() {
        val minaImageUri ="instagram.fgyd5-2.fna.fbcdn.net/v/t51.2885-15/e35/p1080x1080/105418575_570653827216423_7530459996637786778_n.jpg?_nc_ht=instagram.fgyd5-2.fna.fbcdn.net&_nc_cat=107&_nc_ohc=6R2jSdeINRIAX_zNM2v&oh=0ebe2bcf5c73b8460203b12e75757a33&oe=5F20C3F6"
        //val minaImageUri2 ="instagram.fgyd5-2.fna.fbcdn.net/v/t51.2885-15/e35/p1080x1080/101809422_879395235861199_893802089720349895_n.jpg?_nc_ht=instagram.fgyd5-2.fna.fbcdn.net&_nc_cat=107&_nc_ohc=st70qJANwRcAX_dCjwQ&oh=96b9ab03fc83832f0d7b89af3312ab6e&oe=5F1F147F"
        //val imageUrl2 ="scontent.fgyd5-1.fna.fbcdn.net/v/t1.0-9/s960x960/31949112_763679417310136_7575748313534693376_o.jpg?_nc_cat=111&_nc_sid=85a577&_nc_ohc=Hqlm69ENgdIAX_IRuen&_nc_ht=scontent.fgyd5-1.fna&_nc_tp=7&oh=8f1814881278196b94ee506dd3ed131c&oe=5F1BAB53"
        UniversalImageLoader.setImage(minaImageUri, imgUserProfilePhotos, progressBarEditFragment, "https://")

    }




}