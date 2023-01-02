package com.example.mvcprac.repository;

import com.example.mvcprac.model.Meeting;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    boolean existsByPath(String path);

    @EntityGraph(value = "Meeting.withAll", type = EntityGraph.EntityGraphType.LOAD)
     Meeting findByPath(String path);
    @EntityGraph(value = "Meeting.withTagsAndManagers", type = EntityGraph.EntityGraphType.FETCH)
    Meeting findMeetingWithTagsByPath(String path);
    @EntityGraph(value = "Meeting.withZonesAndManagers", type = EntityGraph.EntityGraphType.FETCH)
    Meeting findMeetingWithZonesByPath(String path);
    @EntityGraph(value = "Meeting.withManagers", type = EntityGraph.EntityGraphType.FETCH)
    Meeting findMeetingWithManagersByPath(String path);
    @EntityGraph(value = "Meeting.withMembers", type = EntityGraph.EntityGraphType.FETCH)
    Meeting findMeetingWithMembersByPath(String path);

    Meeting findMeetingOnlyByPath(String path);
}