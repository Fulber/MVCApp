package ro.teamnet.zth.appl.service;

import ro.teamnet.zth.appl.domain.Job;

import java.util.List;

/**
 * Created by user on 15.07.2016.
 */
public interface JobService {

    List<Job> findAllJobs();

    Job findOneJob(String id);

    void deleteOneJob(String id);

    Job insertOneJob(Job job);

    Job updateOneJob(Job job);
}
