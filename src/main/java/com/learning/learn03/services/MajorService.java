package com.learning.learn03.services;

import com.learning.learn03.base.BaseService;
import com.learning.learn03.interfaces.IMajorService;
import com.learning.learn03.models.Major;
import com.learning.learn03.repositories.MajorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MajorService extends BaseService<Major, Integer> implements IMajorService {

    private final MajorRepository majorRepository;

    protected MajorService(JpaRepository<Major, Integer> repository, MajorRepository majorRepository) {
        super(repository);
        this.majorRepository = majorRepository;
    }

    @Override
    protected void prePersist(Major major) {
        if (majorRepository.majorWithThisNameExist(major.getMajorName())){
            throw new RuntimeException("Major with name " + major.getMajorName() + " already exists!");
        }
        major.setMajorCode(UUID.randomUUID());
        major.setMajorActive(true);
    }

    @Override
    protected void preUpdate(Major major) {
        Major foundedMajor = majorRepository.findById(major.getId())
                .orElseThrow(() -> new EntityNotFoundException("%s not found!"));
        major.setUpdatedAt(foundedMajor.getUpdatedAt());
        major.setVersion(foundedMajor.getVersion());
    }

    @Override
    public void delete(int aLong) {
        Major major = majorRepository.findById(aLong)
                .orElseThrow(() -> new EntityNotFoundException(String.format("%s not found!", "Major")));
        major.setMajorActive(false);
        majorRepository.save(major);
    }

    @Override
    public List<Major> findAll() {
        List<Major> majors = majorRepository.findAll();
        List<Major> result = new ArrayList<>();
        for (Major major : majors) {
            if (!major.isMajorActive()) {
                result.add(major);
            }
        }
        return result;
    }

    @Override
    public Major findById(int aLong) {
        Major major = majorRepository.findById(aLong)
                .orElseThrow(() -> new EntityNotFoundException(String.format("%s not found!", "Major")));
        if (major.isMajorActive()) {
            throw new EntityNotFoundException(String.format("%s not found!", "Major"));
        }
        return major;
    }
}
