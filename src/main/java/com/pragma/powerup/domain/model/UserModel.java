package com.pragma.powerup.domain.model;

import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.model.valueobject.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserModel {

    private Long id;
    private UserName name;
    private UserLastName lastName;
    private UserDocumentNumber documentNumber;
    private UserPhone phone;
    private UserBirthDate birthDate;
    private UserEmail email;
    private UserPassword password;
    private RoleModel role;
    private RestaurantId restaurantId;

    private UserModel(Builder builder) {
        this.id = builder.id;
        this.name = new UserName(builder.name);
        this.lastName = new UserLastName(builder.lastName);
        this.documentNumber = new UserDocumentNumber(builder.documentNumber);
        this.phone = new UserPhone(builder.phone);
        this.email = new UserEmail(builder.email);
        this.password = new UserPassword(builder.password);
        this.birthDate = builder.birthDate != null ? new UserBirthDate(builder.birthDate) : null;
        this.role = builder.role;
        this.restaurantId = builder.restaurantId != null ? new RestaurantId(builder.restaurantId) : null;
    }

    private UserModel() {}

    public static Builder builder() {
        return new Builder();
    }

    public static UserModel reconstruct(Long id, String name, String lastName,
                                        String documentNumber, String phone,
                                        LocalDate birthDate, String email,
                                        String password, RoleModel role,
                                        Long restaurantId) {
        UserModel model = new UserModel();
        model.id = id;
        model.name = new UserName(name);
        model.lastName = new UserLastName(lastName);
        model.documentNumber = new UserDocumentNumber(documentNumber);
        model.phone = new UserPhone(phone);
        model.email = new UserEmail(email);
        model.password = new UserPassword(password);
        model.birthDate = birthDate != null ? new UserBirthDate(birthDate) : null;
        model.role = role;
        model.restaurantId = restaurantId != null ? new RestaurantId(restaurantId) : null;
        return model;
    }

    public Long getId()                       { return id; }
    public UserName getName()                 { return name; }
    public UserLastName getLastName()         { return lastName; }
    public UserDocumentNumber getDocumentNumber() { return documentNumber; }
    public UserPhone getPhone()               { return phone; }
    public UserBirthDate getBirthDate()       { return birthDate; }
    public UserEmail getEmail()               { return email; }
    public UserPassword getPassword()         { return password; }
    public RoleModel getRole()                { return role; }
    public RestaurantId getRestaurantId()      { return restaurantId; }

    public static class Builder {

        private Long id;
        private String name;
        private String lastName;
        private String documentNumber;
        private String phone;
        private LocalDate birthDate;
        private String email;
        private String password;
        private RoleModel role;
        private Long restaurantId;

        public Builder id(Long id)                       { this.id = id; return this; }
        public Builder name(String name)                 { this.name = name; return this; }
        public Builder lastName(String lastName)         { this.lastName = lastName; return this; }
        public Builder documentNumber(String doc)        { this.documentNumber = doc; return this; }
        public Builder phone(String phone)               { this.phone = phone; return this; }
        public Builder birthDate(LocalDate birthDate)    { this.birthDate = birthDate; return this; }
        public Builder email(String email)               { this.email = email; return this; }
        public Builder password(String password)         { this.password = password; return this; }
        public Builder role(RoleModel role)              { this.role = role; return this; }
        public Builder restaurantId(Long restaurantId)   { this.restaurantId = restaurantId; return this; }

        public UserModel build() {
            Map<String, String> errors = new LinkedHashMap<>();

            UserName.validate(name, errors);
            UserLastName.validate(lastName, errors);
            UserDocumentNumber.validate(documentNumber, errors);
            UserPhone.validate(phone, errors);
            UserEmail.validate(email, errors);
            UserPassword.validate(password, errors);
            UserBirthDate.validate(birthDate, role != null ? role.getId() : null, errors);
            RestaurantId.validate(restaurantId, role != null ? role.getId() : null, errors);

            if (!errors.isEmpty())
                throw new FieldsValidationException(errors);

            return new UserModel(this);
        }
    }
}
