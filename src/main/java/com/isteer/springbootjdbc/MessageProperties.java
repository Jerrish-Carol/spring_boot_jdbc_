package com.isteer.springbootjdbc;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component

public class MessageProperties implements EnvironmentAware {
	
 public static MessageSource messageSource;

	public static Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		MessageProperties.environment = environment;
	}

	private static final String BAD_SQL_SYNTAX_MESSAGE = "error.badsqlsyntax";
	private static final String DETAILS_NOT_PROVIDED_MESSAGE = "error.detailsnotprovided";
	private static final String CONSTRAINS_INVALID_MESSAGE = "error.constrainsinvalid";
	private static final String NO_CONTENT_TO_DELETE_MESSAGE = "error.nocontenttodelete";
	private static final String NO_CONTENT_TO_UPDATE_MESSAGE = "error.nocontenttoupdate";
	private static final String DUPLICATE_KEY_MESSAGE = "error.duplicatekey";
	private static final String DETAILS_DISPLAYED_MESSAGE = "success.detailsdisplayed";
	private static final String FIELD_VALIDATED_MESSAGE = "success.fieldsvalidated";
	private static final String DETAILS_DELETED_MESSAGE = "success.detailsdeleted";
	private static final String DETAILS_SAVED_MESSAGE = "success.detailssaved";
	private static final String DETAILS_UPDATED_MESSAGE = "success.detailsupdated";
	private static final String NOT_FOUND_MESSAGE = "error.notfound";
	private static final String JSON_PROCESS_EXCEPTION_MESSAGE = "error.jsonprocessexception";

	public MessageProperties(@Qualifier("messageSource") MessageSource messageSource) {
		MessageProperties.messageSource = messageSource;
	}
	
	public String getJsonParseExceptionMessage() {
		return messageSource.getMessage(JSON_PROCESS_EXCEPTION_MESSAGE, null, Locale.getDefault());
	}

	public String getBadSqlSyntaxErrorMessage() {
		return messageSource.getMessage(BAD_SQL_SYNTAX_MESSAGE, null, Locale.getDefault());
	}

	public String getDetailsNotProvidedMessage() {
		return messageSource.getMessage(DETAILS_NOT_PROVIDED_MESSAGE, null, Locale.getDefault());
	}

	public String getConstraintsInvalidMessage() {
		return messageSource.getMessage(CONSTRAINS_INVALID_MESSAGE, null, Locale.getDefault());
	}

	public String getNoContentToDeleteMessage() {
		return messageSource.getMessage(NO_CONTENT_TO_DELETE_MESSAGE, null, Locale.getDefault());
	}

	public String getNoContentToUpdateMessage() {
		return messageSource.getMessage(NO_CONTENT_TO_UPDATE_MESSAGE, null, Locale.getDefault());
	}

	public String getDuplicateKeyMessage() {
		return messageSource.getMessage(DUPLICATE_KEY_MESSAGE, null, Locale.getDefault());
	}

	public String getDetailsDisplayedMessage() {
		return messageSource.getMessage(DETAILS_DISPLAYED_MESSAGE, null, Locale.getDefault());
	}

	public String getDetailsDeletedMessage() {
		return messageSource.getMessage(DETAILS_DELETED_MESSAGE, null, Locale.getDefault());
	}

	public String getDetailsSavedMessage() {
		return messageSource.getMessage(DETAILS_SAVED_MESSAGE, null, Locale.getDefault());
	}

	public String getDetailsUpdatedMessage() {
		return messageSource.getMessage(DETAILS_UPDATED_MESSAGE, null, Locale.getDefault());
	}

	public String getFieldValidatedMessage() {
		return messageSource.getMessage(FIELD_VALIDATED_MESSAGE, null, Locale.getDefault());
	}

	public String getNotFoundMessage() {
		return messageSource.getMessage(NOT_FOUND_MESSAGE, null, Locale.getDefault());
	}
}
