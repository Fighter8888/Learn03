package com.learning.learn03.controllers;

import com.learning.learn03.interfaces.IStudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/web/student")
public class StudentController {


    private final IStudentService iStudentService;

    public StudentController(IStudentService iStudentService) {
        this.iStudentService = iStudentService;
    }


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/take/course/{courseId}")
    public ResponseEntity<ApiResponseDTO> studentGetCourse(@PathVariable Integer courseId, Principal principal) {
        iStudentService.CourseRequest(courseId, principal);
        return ResponseEntity.ok(new ApiResponseDTO("Course get success.", true));
    }


//    private final StudentService iStudentService;
//    private final ExamService examService;
//    private final AnswerMapper answerMapper;
//    private final AnswerService answerService;
//
//    public StudentController(StudentService iStudentService,
//                             ExamService examService,
//                             AnswerMapper answerMapper,
//                             AnswerService answerService) {
//        this.iStudentService = iStudentService;
//        this.examService = examService;
//        this.answerMapper = answerMapper;
//        this.answerService = answerService;
//    }
//
//    @PreAuthorize("hasRole('STUDENT')")
//    @PostMapping("/take/course/{courseId}")
//    public ResponseEntity<ApiResponseDTO> studentGetCourse(@PathVariable Long courseId, Principal principal) {
//        iStudentService.studentTakeCourse(courseId, principal);
//        return ResponseEntity.ok(new ApiResponseDTO("Course get success.", true));
//    }
//
//    @PreAuthorize("hasRole('STUDENT')")
//    @PostMapping("/start/exam/{examId}")
//    public ResponseEntity<ApiResponseDTO> studentStartExam(@PathVariable Long examId, Principal principal) {
//        examService.startExam(examId, principal);
//        return ResponseEntity.ok(new ApiResponseDTO("Exam start success.", true));
//    }
//
//    @PreAuthorize("hasRole('STUDENT')")
//    @PostMapping("/submit/exam/{examId}")
//    public ResponseEntity<ApiResponseDTO> studentSubmitExam(@PathVariable Long examId, Principal principal) {
//        examService.submitExam(examId, principal);
//        return ResponseEntity.ok(new ApiResponseDTO("Exam submit success.", true));
//    }
//
//    @PreAuthorize("hasRole('STUDENT')")
//    @PostMapping("/save/answer")
//    public ResponseEntity<ApiResponseDTO> submitAnswer(@RequestBody AnswerDTO answerDTO , Principal principal) {
//        Answer answer = answerMapper.toEntity(answerDTO , principal);
//        Option option = new Option();
//        if (answerDTO.getOptionId() != null) {
//         option = answerService.findOptionById(answerDTO.getOptionId());
//        }
//        answerService.saveAnswer(answerDTO.getType() , answer, option, answerDTO.getAnswerText());
//        return ResponseEntity.ok(new ApiResponseDTO("Answer saved success.", true));
//    }
}
