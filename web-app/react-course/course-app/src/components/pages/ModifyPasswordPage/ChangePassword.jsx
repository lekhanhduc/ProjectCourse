import React, { useEffect, useState } from 'react';
import { FaEye, FaEyeSlash, FaLock, FaKey, FaUser, FaShieldAlt, FaSync } from 'react-icons/fa';
import { ToastContainer, toast } from 'react-toastify';
import { motion } from 'framer-motion';

export const ChangePassword = () => {

  const token = localStorage.getItem('token');
  const [showCurrentPassword, setShowCurrentPassword] = useState(false);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const [isSnipping, setIsSnipping] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    document.title = 'Change Password'
  });

  const togglePasswordVisibility = (setter) => {
    setter((prev) => !prev);
  };

  const submitChangePassword = (event) => {
    event.preventDefault();
    setIsLoading(true);
    setTimeout(() => {
      setIsSnipping(false);
    }, 1000);

    fetch(`http://localhost:8080/api/v1/change-password`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify({ currentPassword, newPassword, confirmPassword }),
    })
      .then(async (response) => {
        setIsLoading(false);
        if (response.ok) {
          toast.success('Password changed successfully.');
        } else {
          const errorData = await response.json();
          toast.error(errorData.message);
        }
      })
      .catch((error) => {
        setIsLoading(false);
        toast.error('An error occurred. Please try again.');
      });
  };

  if (isSnipping) {
    return (<div>Loading...</div>);
  }

  return (
    <div className='change-password-page'>
      <div className="container py-5">
        <div className="row justify-content-center">
          <div className="col-12 col-md-8 col-lg-6">
            <div className="card shadow-lg border-0 rounded-4">
              <div className="card-header bg-gradient text-white text-center rounded-top p-4" style={{ background: 'linear-gradient(135deg, #6e8efb, #a777e3)' }}>
                <h3 className="font-weight-bold">
                  <FaUser className="me-2" /> Change Password
                </h3>
              </div>
              <div className="card-body p-5">
                <form onSubmit={submitChangePassword}>
                  {/* Current Password Field */}
                  <motion.div
                    className="mb-4 position-relative"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 0.5 }}
                  >
                    <label htmlFor="currentPassword" className="form-label">Current Password</label>
                    <div className="input-group">
                      <span className="input-group-text bg-white border-end-0"><FaLock /></span>
                      <input
                        type={showCurrentPassword ? 'text' : 'password'}
                        className="form-control form-control-lg border-start-0 shadow-sm"
                        id="currentPassword"
                        placeholder="Enter your current password"
                        autoComplete="current-password"
                        onChange={(event) => setCurrentPassword(event.target.value)}
                      />
                      <button
                        type="button"
                        className="btn btn-outline-secondary"
                        onClick={() => togglePasswordVisibility(setShowCurrentPassword)}
                      >
                        {showCurrentPassword ? <FaEyeSlash /> : <FaEye />}
                      </button>
                    </div>
                  </motion.div>

                  {/* New Password Field */}
                  <motion.div
                    className="mb-4 position-relative"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 0.5 }}
                  >
                    <label htmlFor="newPassword" className="form-label">New Password</label>
                    <div className="input-group">
                      <span className="input-group-text bg-white border-end-0"><FaKey /></span>
                      <input
                        type={showNewPassword ? 'text' : 'password'}
                        className="form-control form-control-lg border-start-0 shadow-sm"
                        id="newPassword"
                        placeholder="Enter your new password"
                        autoComplete="new-password"
                        onChange={(event) => setNewPassword(event.target.value)}
                      />
                      <button
                        type="button"
                        className="btn btn-outline-secondary"
                        onClick={() => togglePasswordVisibility(setShowNewPassword)}
                      >
                        {showNewPassword ? <FaEyeSlash /> : <FaEye />}
                      </button>
                    </div>
                  </motion.div>

                  {/* Confirm Password Field */}
                  <motion.div
                    className="mb-4 position-relative"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 0.5 }}
                  >
                    <label htmlFor="confirmPassword" className="form-label">Confirm New Password</label>
                    <div className="input-group">
                      <span className="input-group-text bg-white border-end-0"><FaKey /></span>
                      <input
                        type={showConfirmPassword ? 'text' : 'password'}
                        className="form-control form-control-lg border-start-0 shadow-sm"
                        id="confirmPassword"
                        autoComplete="confirm-password"
                        placeholder="Confirm your new password"
                        onChange={(event) => setConfirmPassword(event.target.value)}
                      />
                      <button
                        type="button"
                        className="btn btn-outline-secondary"
                        onClick={() => togglePasswordVisibility(setShowConfirmPassword)}
                      >
                        {showConfirmPassword ? <FaEyeSlash /> : <FaEye />}
                      </button>
                    </div>
                  </motion.div>

                  {/* Submit Button with loading effect */}
                  <motion.div
                    className="d-grid mt-4"
                    initial={{ scale: 1 }}
                    animate={{ scale: isLoading ? 1.05 : 1 }}
                    transition={{ duration: 0.3 }}
                  >
                    <button
                      type="submit"
                      className="btn btn-primary btn-lg rounded-pill shadow"
                      disabled={isLoading} // Disable button khi đang gửi yêu cầu
                    >
                      {isLoading ? (
                        <FaSync className="me-2 fa-spin" /> // Biểu tượng đang xoay
                      ) : (
                        <FaShieldAlt className="me-2" />
                      )}
                      {isLoading ? 'Processing...' : 'Change Password'}
                    </button>
                  </motion.div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>

      <ToastContainer
        position="top-right"
        autoClose={3000}
        className="custom-toast-container"
      />
    </div>
  );
};
