package com.coding.githubrepoapp.presentation.githubrepo_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.coding.githubrepoapp.R
import com.coding.githubrepoapp.databinding.FragmentRepoDetailBinding
import com.coding.githubrepoapp.databinding.FragmentRepoListBinding

import com.coding.githubrepoapp.domain.model.GithubRepo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepoDetailFragment : Fragment(R.layout.fragment_repo_detail) {

    private var _binding: FragmentRepoDetailBinding? = null
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoDetailBinding.inflate(inflater, container, false)
        arguments?.let {
            val githubRepo = it.getParcelable<GithubRepo>("githubRepo")
            if (githubRepo != null) {
                binding!!.githubrepoName.text = githubRepo.name
                binding!!.githubrepoCreateDate.text = githubRepo.created_at
                binding!!.githubrepoStargazers.text = githubRepo.stargazers_count.toString()
                Glide.with(binding!!.root).load(githubRepo.owner?.avatar_url)
                    .into(binding!!.imGithubRepo)

            }
        }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}