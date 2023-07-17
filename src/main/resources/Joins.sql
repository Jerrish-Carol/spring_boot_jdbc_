DELIMITER //

CREATE PROCEDURE GETALLDETAILS()
BEGIN
    SELECT
        e.id, e.name, e.dob, e.gender, e.isActive, e.isAccountLocked, e.email, e.department, e.roleId,
        addresses.json_addresses AS addresses,
        roles.json_roles AS roles
    FROM
        tbl_employee e
    INNER JOIN (
        SELECT
            employeeId,
            JSON_ARRAYAGG(JSON_OBJECT('addressId', a.addressId, 'employeeId', a.employeeId, 'street', a.street, 'city', a.city, 'state', a.state, 'country', a.country)) AS json_addresses
        FROM
            tbl_address a
        GROUP BY
            employeeId
    ) addresses ON e.id = addresses.employeeId
    INNER JOIN (
        SELECT
            roleId, 
            JSON_ARRAYAGG(JSON_OBJECT('roles', r.role, 'roleId', r.roleId, 'project', r.project, 'billable', r.billable, 'hierarchicalLevel', r.hierarchicalLevel, 'buName', r.buName, 'buHead', r.buHead, 'hrManager', r.hrManager)) AS json_roles
        FROM
            tbl_roles r
        GROUP BY
            roleId
    ) roles ON e.roleId = roles.roleId;
END//

DELIMITER ;
