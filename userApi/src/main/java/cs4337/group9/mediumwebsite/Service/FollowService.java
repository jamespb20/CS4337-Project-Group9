package cs4337.group9.mediumwebsite.Service;

import cs4337.group9.mediumwebsite.DTO.UserDTO;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs4337.group9.mediumwebsite.DTO.EmailDetailsDto;
import cs4337.group9.mediumwebsite.Entity.FollowEntity;
import cs4337.group9.mediumwebsite.Entity.UserEntity;
import cs4337.group9.mediumwebsite.Repostiory.FollowRepository;
import cs4337.group9.mediumwebsite.Repostiory.UserRepository;
import jakarta.transaction.Transactional;


@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SendMailService sendMailService;

    @Transactional
    public String followUser(UUID followerId, UUID followingId) {
        if (followerId.equals(followingId))
            throw new IllegalArgumentException("Cannot follow themself");

        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId))
            return getFollowMessage(followerId, followingId, "is already following");

        UserEntity follower = userRepository.findById(followerId).orElseThrow (() -> new IllegalArgumentException("FollowerId not found"));
        UserEntity following = userRepository.findById(followingId).orElseThrow (() -> new IllegalArgumentException("FollowingId not found"));

        EmailDetailsDto emailDetails = new EmailDetailsDto();
emailDetails.setRecipient(following.getEmail());

try {
    sendMailService.sendFollowNotificationMail(emailDetails, follower.getUsername());
    System.out.println("Email sending logic was executed successfully.");
} catch (Exception e) {
    System.err.println("Failed to send follow notification email: " + e.getMessage());
    e.printStackTrace();
}

        return String.format("%s (%s) has successfully followed %s (%s), email sent.",
                follower.getUsername(), follower.getId(), following.getUsername(), following.getId());
    }
    @Transactional
    public String unfollowUser(UUID followerId, UUID followingId) {
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId))
            return getFollowMessage(followerId, followingId, "is already not following");

        UserEntity follower = userRepository.findById(followerId).orElseThrow (() -> new IllegalArgumentException(String.format("FollowerId not found: %s", followerId)));
        UserEntity following = userRepository.findById(followingId).orElseThrow (() -> new IllegalArgumentException(String.format("FollowingId not found: %s", followingId)));

        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);

        return String.format("%s (%s) has successfully unfollowed %s (%s)",
                follower.getUsername(), follower.getId(), following.getUsername(), following.getId());
    }

    private String getFollowMessage(UUID followerId, UUID followingId, String response) {
        UserEntity follower = userRepository.findById(followerId).orElseThrow (() -> new IllegalArgumentException("FollowerId not found"));
        UserEntity following = userRepository.findById(followingId).orElseThrow (() -> new IllegalArgumentException("FollowingId not found"));

        return String.format("%s (%s) %s %s (%s)",
                follower.getUsername(), follower.getId(), response, following.getUsername(), following.getId());
    }


    public List<UserDTO> getFollowers(UUID userId) {
        List<FollowEntity> relationship = followRepository.findByFollowingId(userId);

        return relationship.stream()
                .map(follow -> userRepository.findById(follow.getFollowerId()).orElse(null))
                .filter(userEntity -> userEntity != null) // Avoid null results
                .map(userService::userEntityToUserDto) // Convert UserEntity to UserDTO
                .collect(Collectors.toList());
    }

    public List<UserDTO> getFollowing(UUID userId) {
        List<FollowEntity> relationship = followRepository.findByFollowerId(userId);

        return relationship.stream()
                .map(follow -> userRepository.findById(follow.getFollowingId()).orElse(null))
                .filter(userEntity -> userEntity != null) // Avoid null results
                .map(userService::userEntityToUserDto) // Convert UserEntity to UserDTO
                .collect(Collectors.toList());
    }
}
