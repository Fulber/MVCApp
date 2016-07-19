package ro.teamnet.zth.appl.service.impl;

import ro.teamnet.zth.api.annotations.MyService;
import ro.teamnet.zth.appl.dao.JobDao;
import ro.teamnet.zth.appl.domain.Job;
import ro.teamnet.zth.appl.service.JobService;

import java.util.List;

/**
 * Created by user on 15.07.2016.
 */
@MyService(name = "job")
public class JobServiceImpl implements JobService {
    JobDao jobDao = new JobDao();

    @Override
    public List<Job> findAllJobs() {
        return jobDao.getAllJobs();
    }

    @Override
    public Job findOneJob(String id) {
        return jobDao.getJobById(id);
    }

    @Override
    public void deleteOneJob(String id) {
        jobDao.deleteJob(jobDao.getJobById(id));
    }

    @Override
    public Job insertOneJob(Job job) {
        return jobDao.insertJob(job);
    }

    @Override
    public Job updateOneJob(Job job) {
        return jobDao.updateJob(job);
    }
}
