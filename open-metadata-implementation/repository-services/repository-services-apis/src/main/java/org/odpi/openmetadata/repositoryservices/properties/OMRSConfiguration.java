/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.properties;

/**
 * Allows applications to programmatically configure some aspects
 * of Egeria using their values from their own configuration
 * mechanism.
 */
public class OMRSConfiguration {
    
    private static final OMRSConfiguration INSTANCE = new OMRSConfiguration();
    
    private int numCohortInitRetryThreads = 1;
    private int maxCohortInitAttempts = 20;
    private long minCohortInitAttemptRetryIntervalMs = 10000;
    
    /**
     * Gets the number of threads in the thread pool used to
     * retry failed cohort initializations.  The thread pool is
     * shared between all cohorts.  The default is one thread.
     */
    public int getNumberOfCohortInitRetryThreads() {
    
        return numCohortInitRetryThreads;
    }
    /**
     * Sets the number of threads in the thread pool used to
     * retry failed cohort initializations.  The thread pool is
     * shared between all cohorts.  The default is one thread.
     * @param numInitThreads
     */
    public void setNumberOfCohortInitRetryThreads(int numInitThreads) {
    
        this.numCohortInitRetryThreads = numInitThreads;
    }
    
    /**
     * Gets the maximum number of attempts to initialize a cohort.  If
     * the value is less than 1, there is no limit on the number of
     * retry attempts.  The default is 20.
     */
    public int getMaxCohortInitAttempts() {
    
        return maxCohortInitAttempts;
    }
     
    /**
     * Sets the maximum number of attempts to initialize a cohort.  If
     * the value is less than 1, there is no limit on the number of
     * retry attempts.  The default is 20.
     * 
     * @param maxInitAttempts
     */
    public void setMaxInitAttempts(int maxInitAttempts) {
    
        this.maxCohortInitAttempts = maxInitAttempts;
    }
    
    /**
     * Gets the number of milliseconds to wait after a cohort initialization
     * fails before trying again.  Note that we won't actually perform the
     * retry until a retry thread from the pool is available, so the
     * actual time between retries may end up being longer than what
     * is configured.
     * 
     */
    public long getCohortInitAttemptRetryIntervalMs() {
    
        return minCohortInitAttemptRetryIntervalMs;
    }
    
    /**
     * Sets the number of milliseconds to wait after a cohort initialization
     * fails before trying again.  Note that we won't actually perform the
     * retry until a retry thread from the pool is available, so the
     * actual time between retries may end up being longer than what
     * is configured.
     * 
     * @param initAttemptRetryInterval
     */
    public void setMinInitAttemptRetryIntervalMs(long initAttemptRetryInterval) {
    
        this.minCohortInitAttemptRetryIntervalMs = initAttemptRetryInterval;
    }
    
    /**
     * Gets the singleton {@link OMRSConfiguration} instance
     * @return
     */
    public static OMRSConfiguration getInstance() {
    
        return INSTANCE;
    }
}