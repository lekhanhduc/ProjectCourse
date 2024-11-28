import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { createPasswordForFirst } from "../../../service/UserService";

export const CreatePassword = () => {

    const [createPassword, setCreatePassword] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        document.title = 'Create Password'
    })

    const handleCreatePassword = (event) => {
        event.preventDefault();
        setPasswordError("");

        if(createPassword.length < 6){
            setPasswordError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }

        createPasswordForFirst(createPassword)
       .then((data) => {
            alert(data.message || "Mật khẩu đã được tạo thành công!");
            navigate("/home");
        }).catch((error) => {
            if (error.message.includes("INVALID_PASSWORD")) {
                setPasswordError(error.message);
            } else {
                alert(error.message);
            }
        });
    };

    return (
        <div className="content-page">
            <div className="container d-flex justify-content-center">
                <div className="card p-4 shadow-lg" style={{ maxWidth: '500px', width: '100%', marginTop: '50px' }}>
                    <div className="card-body text-center">
                        <div className="mb-4">
                            <i className="bi bi-shield-lock-fill" style={{ fontSize: '3rem', color: '#007bff' }}></i>
                            <h2 className="card-title mt-3">Create Your Password</h2>
                        </div>
                        <form id="create-password" onSubmit={handleCreatePassword}>
                            <div className="form-floating mb-3">
                                <input 
                                    type="password" 
                                    className="form-control" 
                                    id="password" 
                                    placeholder="Password" 
                                    value={createPassword}
                                    onChange={(event) => setCreatePassword(event.target.value)}
                                />
                                <label htmlFor="password"><i className="bi bi-lock-fill me-2"></i>Password</label>
                                {passwordError && (
                                    <div className="text-danger mt-1">
                                        {passwordError}
                                    </div>
                                )}
                            </div>

                            <button type="submit" className="btn btn-primary w-100 btn-lg">Create Password</button>
                        </form>
                    </div>
                </div>
            </div>
            
        </div>
    );
}
