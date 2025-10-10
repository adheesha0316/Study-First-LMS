package com.campus.lms.service.Impl;

import com.campus.lms.dto.CourseDto;
import com.campus.lms.dto.StudentDto;
import com.campus.lms.entity.Course;
import com.campus.lms.entity.Student;
import com.campus.lms.repo.CourseRepo;
import com.campus.lms.repo.StudentRepo;
import com.campus.lms.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public StudentServiceImpl(StudentRepo studentRepo, CourseRepo courseRepo, ModelMapper modelMapper) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public StudentDto registerStudent(StudentDto studentDto) {
        Student student = modelMapper.map(studentDto, Student.class);
        Student saved = studentRepo.save(student);
        return modelMapper.map(saved, StudentDto.class);
    }

    @Override
    public StudentDto getProfile(Integer studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return modelMapper.map(student, StudentDto.class);
    }

    @Override
    public StudentDto updateProfile(Integer studentId, StudentDto studentDto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // update allowed fields
        student.setName(studentDto.getName());
        student.setAge(studentDto.getAge());
        student.setPhone(studentDto.getPhone());
        student.setAddress(studentDto.getAddress());

        // handle profile image file upload if provided
        if (studentDto.getProfileImageFile() != null && !studentDto.getProfileImageFile().isEmpty()) {
            try {
                String folder = "uploads/students/";
                Path path = Paths.get(folder + studentDto.getProfileImageFile().getOriginalFilename());
                Files.createDirectories(path.getParent());
                Files.write(path, studentDto.getProfileImageFile().getBytes());

                student.setProfileImage(path.toString()); // save the image path in DB
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile image", e);
            }
        }

        Student updated = studentRepo.save(student);
        return modelMapper.map(updated, StudentDto.class);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepo.findAll()
                .stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public String enrollCourse(Integer studentId, Integer courseId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        student.getCourses().add(course);
        studentRepo.save(student);

        return "Student enrolled in course successfully!";
    }

    @Override
    public String unenrollCourse(Integer studentId, Integer courseId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        student.getCourses().remove(course);
        studentRepo.save(student);

        return "Student unenrolled from course successfully!";
    }

    @Override
    public List<String> getCourseMaterials(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getMaterials(); // assuming `Course` has `List<String> materials`
    }

    @Override
    public List<String> getCourseRecordings(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getRecordings(); // assuming `Course` has `List<String> recordings`
    }

    @Override
    public List<String> getAssignments(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getAssignments(); // assuming `Course` has `List<String> assignments`
    }

    @Override
    public List<String> getQuizzes(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getQuizzes(); // assuming `Course` has `List<String> quizzes`
    }

    @Override
    public List<String> getExams(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getExams(); // assuming `Course` has `List<String> exams`
    }

    @Override
    public String uploadPaymentSlip(Integer studentId, MultipartFile file) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        try {
            String folder = "uploads/payment-slips/";
            Path path = Paths.get(folder + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            student.setPaymentSlip(path.toString()); // store path in DB
            studentRepo.save(student);

            return "Payment slip uploaded successfully!";
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload payment slip", e);
        }
    }
}
