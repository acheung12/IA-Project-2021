package com.example.iasubstituteteacher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 *
 */

public class TimePickerFragment extends DialogFragment
{
    private static final String KEY_TIMEPICKER_TAG = "KEY_TIMEPICKER_TAG";

    public static TimePickerFragment instance(String timePickerTag)
    {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle b = new Bundle();
        b.putString(KEY_TIMEPICKER_TAG, timePickerTag);
        fragment.setArguments(b);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        return new TimePickerDialog(getActivity(), style, (TimePickerDialog.OnTimeSetListener)
                getActivity(), hour, minute, android.text.format.DateFormat.is24HourFormat
                (getActivity()));
    }
}
