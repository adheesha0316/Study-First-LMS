package com.campus.lms.service.Impl;

import com.campus.lms.dto.*;
import com.campus.lms.entity.*;
import com.campus.lms.enums.CourseStatus;
import com.campus.lms.enums.EnrollmentStatus;
import com.campus.lms.enums.PaymentStatus;
import com.campus.lms.repo.*;
import com.campus.lms.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final EnrollmentRepo enrollmentRepo;
    private final AssignmentRepo assignmentRepo;
    private final AssignmentSubmissionRepo submissionRepo;
    private final PaymentRepo paymentRepo;
    private final ModelMapper modelMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public StudentServiceImpl(StudentRepo studentRepo, UserRepo userRepo, CourseRepo courseRepo, EnrollmentRepo enrollmentRepo, AssignmentRepo assignmentRepo, AssignmentSubmissionRepo submissionRepo, PaymentRepo paymentRepo, ModelMapper modelMapper) {
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.assignmentRepo = assignmentRepo;
        this.submissionRepo = submissionRepo;
        this.paymentRepo = paymentRepo;
        this.modelMapper = modelMapper;
    }


    // -------------------- Student Profile --------------------
    @Override
    public StudentRegisterDto registerStudent(StudentRegisterDto dto) {
        // Load the user
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map DTO to Student
        Student student = modelMapper.map(dto, Student.class);

        // Attach the user
        student.setUser(user);

        // Save
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

    // -------------------- Profile Image --------------------
    @Override
    public String uploadProfileImage(Integer studentId, StudentProfileImageDto dto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        MultipartFile file = dto.getProfileImageFile();
        if (file == null || file.isEmpty()) return "No file provided";

        try {
            // Folder path
            Path folder = Paths.get("uploads/studentProfileImg");
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }

            // Create a unique file name
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = folder.resolve(fileName);

            // Write file to disk
            Files.write(path, file.getBytes(), StandardOpenOption.CREATE);

            // Save relative path (not absolute) in DB
            student.setProfileImage("studentProfileImg/" + fileName);
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

        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new RuntimeException("Course is not active");
        }

        boolean alreadyEnrolled = enrollmentRepo
                .findByStudent_StudentIdAndCourse_CourseId(studentId, dto.getCourseId())
                .isPresent();

        if (alreadyEnrolled) {
            throw new RuntimeException("Student already enrolled");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(EnrollmentStatus.ACTIVE)
                .build();

        enrollmentRepo.save(enrollment);
        return "Student enrolled successfully";
    }

    @Override
    public String unenrollCourse(Integer studentId, Integer courseId) {
        Enrollment enrollment = enrollmentRepo
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepo.save(enrollment);
        return "Enrollment cancelled successfully";
    }

    // -------------------- Payment --------------------
    @Override
    public String uploadPaymentSlip(Integer studentId, PaymentUploadDto dto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        MultipartFile file = dto.getPaymentSlip();
        if (file == null || file.isEmpty()) return "No payment slip provided";

        try {
            Path folder = Paths.get(uploadDir, "paymentSlip");
            Files.createDirectories(folder);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = folder.resolve(fileName);
            Files.write(path, file.getBytes(), StandardOpenOption.CREATE);

            Payment payment = Payment.builder()
                    .student(student)
                    .course(course)
                    .amount(dto.getAmount())
                    .slipPath(path.toString())
                    .paidAt(LocalDateTime.now())
                    .status(PaymentStatus.PENDING)
                    .build();

            paymentRepo.save(payment);
            return "Payment slip uploaded successfully. Waiting for admin approval.";
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload payment slip", e);
        }
    }

    // -------------------- Courses & Materials --------------------
    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepo.findAll()
                .stream()
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
        return course.getRecordings();
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

    // -------------------- Download Assignment --------------------
    @Override
    public Resource downloadAssignment(Integer studentId, Integer assignmentId) {
        // Optional: verify student is enrolled in the course of the assignment
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        String filePath = assignment.getFilePath();
        if (filePath == null || filePath.isEmpty()) {
            throw new RuntimeException("Assignment file not found");
        }

        Path path = Paths.get(filePath);
        Resource resource = new FileSystemResource(path.toFile());

        if (!resource.exists()) {
            throw new RuntimeException("File not found on server");
        }

        return resource;
    }

    @Override
    public String uploadAssignmentSubmission(Integer studentId, Integer assignmentId, AssignmentSubmissionUploadDto dto) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        MultipartFile file = dto.getFile(); // get the file from DTO
        if (file == null || file.isEmpty()) return "No file provided";

        try {
            Path folder = Paths.get("uploads/assignmentSubmissions");
            Files.createDirectories(folder);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = folder.resolve(fileName);

            Files.write(path, file.getBytes(), StandardOpenOption.CREATE);

            AssignmentSubmission submission = AssignmentSubmission.builder()
                    .assignment(assignment)
                    .student(student)
                    .filePath(path.toString())
                    .build();

            submissionRepo.save(submission);

            return "Assignment submitted successfully!";
        } catch (IOException e) {
            throw new RuntimeException("Failed to submit assignment", e);
        }
    }
}
