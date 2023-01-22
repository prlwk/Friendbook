package com.src.book.di

import com.src.book.domain.repository.FriendRepository
import com.src.book.domain.usecase.friend.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainFriendModule {
    @Singleton
    @Provides
    fun provideSendFriendRequestUseCase(friendRepository: FriendRepository): SendFriendRequestUseCase {
        return SendFriendRequestUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideGetIncomingRequestsUseCase(friendRepository: FriendRepository): GetIncomingRequestsUseCase {
        return GetIncomingRequestsUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideSubmitFriendRequestUseCase(friendRepository: FriendRepository): SubmitFriendRequestUseCase {
        return SubmitFriendRequestUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideRejectIncomingFriendRequestUseCase(friendRepository: FriendRepository): RejectIncomingFriendRequestUseCase {
        return RejectIncomingFriendRequestUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideGetOutgoingRequestsUseCase(friendRepository: FriendRepository): GetOutgoingRequestsUseCase {
        return GetOutgoingRequestsUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideRejectOutgoingFriendRequestUseCase(friendRepository: FriendRepository): RejectOutgoingFriendRequestUseCase {
        return RejectOutgoingFriendRequestUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideGetFriendsUseCase(friendRepository: FriendRepository): GetFriendsUseCase {
        return GetFriendsUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideGetIncomingRequestsComingUseCase(friendRepository: FriendRepository): GetIncomingRequestsCountUseCase {
        return GetIncomingRequestsCountUseCase(friendRepository = friendRepository)
    }

    @Singleton
    @Provides
    fun provideRemoveFriendUseCase(friendRepository: FriendRepository): RemoveFriendUseCase {
        return RemoveFriendUseCase(friendRepository = friendRepository)
    }

}