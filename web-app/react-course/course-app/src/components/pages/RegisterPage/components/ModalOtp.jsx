import { useState } from "react";

const OTPModal = ({ handleOtpSubmit, errorMessage, formData }) => {
    const [otp, setOtp] = useState(["", "", "", "", "", ""]);

    const handleChange = (e, index) => {
        const value = e.target.value;
        const newOtp = [...otp];
        newOtp[index] = value;
        setOtp(newOtp);

        if (value && index < otp.length - 1) {
            document.getElementById(`otp-input-${index + 1}`).focus();
        }
    };

    const handleBackspace = (e, index) => {
        if (e.key === 'Backspace' && !otp[index] && index > 0) {
            document.getElementById(`otp-input-${index - 1}`).focus();
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        handleOtpSubmit(otp.join(''));
    };

    return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            <div className="bg-white p-5 rounded-4 shadow-lg border" style={{ width: '500px' }}>
                <div className="text-center mb-4">
                    <p className="text-success">
                        <i className="fa-solid fa-envelope-circle-check"></i>
                    </p>
                    <h4 className="mb-3">Please check your email</h4>
                    <p className="text-muted">We’ve sent a code to {formData.email}</p>
                </div>

                <form onSubmit={handleSubmit}>
                    <div className="d-flex justify-content-center mb-4">
                        {otp.map((digit, index) => (
                            <input
                                key={index}
                                id={`otp-input-${index}`}
                                type="text"
                                maxLength="1"
                                value={digit}
                                onChange={(e) => handleChange(e, index)}
                                onKeyDown={(e) => handleBackspace(e, index)}
                                className="otp-input"
                                style={{
                                    width: '50px',
                                    height: '50px',
                                    textAlign: 'center',
                                    fontSize: '2rem',
                                    margin: '0 10px',
                                    borderRadius: '8px',
                                    border: '1px solid #ddd',
                                }}
                            />
                        ))}
                    </div>

                    <p className="text-muted text-center">
                        Didn’t get the code? <a href="/" className="text-success">Resend.</a>
                    </p>

                    <div className="d-flex justify-content-center">
                        <button className="btn btn-verify-otp" type="submit">Verify Otp</button>
                    </div>
                </form>

                {errorMessage && (
                    <p className="text-danger text-center">{errorMessage}</p>
                )}
            </div>
        </div>
    );
};

export default OTPModal;
