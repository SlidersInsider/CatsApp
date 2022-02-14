package com.mzhadan.catsapp.di

import com.mzhadan.catsapp.MainActivity
import com.mzhadan.catsapp.repository.CatsRepositoryImpl
import com.mzhadan.catsapp.ui.catsDownloadPage.CatsDownloadFragment
import com.mzhadan.catsapp.ui.catsDownloadPage.CatsDownloadViewModel
import com.mzhadan.catsapp.ui.catsFavoritePage.CatsFavoriteFragment
import com.mzhadan.catsapp.ui.catsFavoritePage.CatsFavoriteViewModel
import com.mzhadan.catsapp.ui.catsListPage.CatsListFragment
import com.mzhadan.catsapp.ui.catsListPage.CatsListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [CatsApiServiceModule::class])
@Singleton
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(catsListFragment: CatsListFragment)
    fun inject(catsListViewModel: CatsListViewModel)

    fun inject(catsDownloadFragment: CatsDownloadFragment)
    fun inject(catsDownloadViewModel: CatsDownloadViewModel)

    fun inject(catsFavoriteFragment: CatsFavoriteFragment)
    fun inject(catsFavoriteViewModel: CatsFavoriteViewModel)

    fun inject(catsRepositoryImpl: CatsRepositoryImpl)
}