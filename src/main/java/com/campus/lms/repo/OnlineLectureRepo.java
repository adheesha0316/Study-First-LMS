package com.campus.lms.repo;

import com.campus.lms.entity.OnlineLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnlineLectureRepo extends JpaRepository<OnlineLecture, Integer> {
    List<OnlineLecture> findByLecturer_LecturerId(Integer lecturerId);

    List<OnlineLecture> findByCourse_CourseId(Integer courseId);
}
