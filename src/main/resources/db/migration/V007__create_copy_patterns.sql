create procedure electric_cal.copy_patterns(IN firmId bigint)
begin
insert ignore into firm_pattern(firm_id, pattern_id) select firm_id, id from patterns where firm_id is null;
end;