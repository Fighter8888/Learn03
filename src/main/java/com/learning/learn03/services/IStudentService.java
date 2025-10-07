package com.learning.learn03.services;

import java.security.Principal;

public interface IStudentService {
    void CourseRequest (int courseId, Principal principal);
}
