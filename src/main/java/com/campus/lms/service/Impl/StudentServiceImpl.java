package com.campus.lms.service.Impl;

import com.campus.lms.dto.*;
import com.campus.lms.entity.Course;
import com.campus.lms.entity.Enrollment;
import com.campus.lms.entity.Payment;
import com.campus.lms.entity.Student;
import com.campus.lms.repo.CourseRepo;
import com.campus.lms.repo.StudentRepo;
import com.campus.lms.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public StudentServiceImpl(StudentRepo studentRepo, CourseRepo courseRepo, ModelMapper modelMapper) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.modelMapper = modelMapper;
    }

    // -------------------- Student Registration --------------------
    @Override
    public StudentRegisterDto registerStudent(StudentRegisterDto dto) {
        Student student = modelMapper.map(dto, Student.class);
        // link with existing User if needed
        Student saved = studentRepo.save(student);
        return modelMapper.map(saved, StudentRegisterDto.class);
    }

    @Override
    public StudentRegisterDto getProfile(Integer studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return modelMapper.map(student, StudentRegisterDto.class);
    }

    @Override
    public StudentRegisterDto updateProfile(Integer studentId, StudentUpdateDto dto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setPhone(dto.getPhone());
        student.setAddress(dto.getAddress());

        Student updated = studentRepo.save(student);
        return modelMapper.map(updated, StudentRegisterDto.class);
    }

    // -------------------- Profile Image Upload --------------------
    @Override
    public String uploadProfileImage(Integer studentId, StudentProfileImageDto dto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MultipartFile file = dto.getProfileImageFile();
        if (file == null || file.isEmpty()) return "No file provided";

        try {
            Path folder = Paths.get(uploadDir + "/studentProfileImg/");
            Files.createDirectories(folder);
            Path path = folder.resolve(file.getOriginalFilename());
            Files.write(path, file.getBytes());

            student.setProfileImage(path.toString());
            studentRepo.save(student);

            return "Profile image uploaded successfully!";
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile image", e);
        }
    }

    // -------------------- Course Enrollment --------------------
    @Override
    public String enrollCourse(Integer studentId, EnrollmentDto dto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Create Enrollment
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status("ACTIVE")
                .build();

        student.addEnrollment(enrollment);
        studentRepo.save(student);

        return "Student enrolled in course successfully!";
    }

    @Override
    public String unenrollCourse(Integer studentId, Integer courseId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Enrollment enrollment = student.getEnrollments().stream()
                .filter(e -> e.getCourse().getCourseId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        student.removeEnrollment(enrollment);
        studentRepo.save(student);

        return "Student unenrolled from course successfully!";
    }

    // -------------------- Payment Upload --------------------
    @Override
    public String uploadPaymentSlip(Integer studentId, PaymentUploadDto dto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MultipartFile file = dto.getPaymentSlip();
        if (file == null || file.isEmpty()) return "No file provided";

        try {
            Path folder = Paths.get(uploadDir + "/paymentSlip/");
            Files.createDirectories(folder);
            Path path = folder.resolve(file.getOriginalFilename());
            Files.write(path, file.getBytes());

            Payment payment = Payment.builder()
                    .student(student)
                    .amount(dto.getAmount())
                    .paymentSlip(path.toString())
                    .status("PENDING")
                    .build();

            student.addPayment(payment);
            studentRepo.save(student);

            return "Payment slip uploaded successfully!";
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload payment slip", e);
        }
    }

    // -------------------- Courses & Materials --------------------
    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepo.findAll().stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCourseMaterials(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getMaterials();
    }

    @Override
    public List<String> getCourseRecordings(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getMaterials();
    }

    @Override
    public List<String> getAssignments(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getAssignments();
    }

    @Override
    public List<String> getQuizzes(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getQuizzes();
    }

    @Override
    public List<String> getExams(Integer courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getExams();
    }
}
