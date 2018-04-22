package com.kamenov.martin.gosportbg.menu;

import com.kamenov.martin.gosportbg.navigation.NavigationCommand;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Martin on 22.4.2018 г..
 */

public class MenuPresenterTests {
    @Test
    public void MenuFragment_DoesNotThrow_WhenCreated() {

        // Arrange
        NavigationCommand command = Mockito.mock(NavigationCommand.class);
        MenuPresenter menuPresenter = new MenuPresenter(command);

        // Act && Assert
        Assert.assertNotNull(menuPresenter);
    }

    @Test
    public void MenuFragment_Navigates() {

        // Arrange
        NavigationCommand command = Mockito.mock(NavigationCommand.class);
        MenuPresenter menuPresenter = new MenuPresenter(command);

        // Act
        menuPresenter.navigateToCreateNewEvents();

        // Assert
        verify(command, times(1)).navigate();
    }
}
