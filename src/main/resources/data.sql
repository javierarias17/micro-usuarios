INSERT INTO roles (id, name, description) VALUES (1, 'ADMIN', 'System administrator') ON DUPLICATE KEY UPDATE
                                                                                          name = VALUES(name),
                                                                                          description = VALUES(description);
INSERT INTO roles (id, name, description) VALUES (2, 'OWNER', 'Restaurant owner') ON DUPLICATE KEY UPDATE
                                                                                      name = VALUES(name),
                                                                                      description = VALUES(description);
INSERT INTO roles (id, name, description) VALUES (3, 'EMPLOYEE', 'Restaurant employee') ON DUPLICATE KEY UPDATE
                                                                                            name = VALUES(name),
                                                                                            description = VALUES(description);
INSERT INTO roles (id, name, description) VALUES (4, 'CUSTOMER', 'Food court customer') ON DUPLICATE KEY UPDATE
                                                                                            name = VALUES(name),
                                                                                            description = VALUES(description);
